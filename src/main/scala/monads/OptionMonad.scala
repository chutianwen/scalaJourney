package monads

import scala.collection.mutable

/**
  *  It is not uncommon for methods or data structures of type Option[T] to be revamped to something like Seq[T].
  *  While code using pattern matching would have to be rewritten, code using only higher-order functions or for-comprehensions may not require
  *  any changes at all. Most higher-order function and for-comprehension idioms are consistent across all collection types.
  *
  *  1. Prefer Option(a) over Some(a)
  *  2. forall(isPrime) == !exists(!isPrime), So None.forall(p) == !None.exist(!p)
  *

  */
class OptionMonad {

  case class ContactInformation(email: Option[Email])
  case class Email(address: String, verified: Boolean)

  /**
    * Using for-comprehension to extract option monad
    * @param id
    * @return
    */
  def emailInformation(id: Int): Option[(String, String)] = for {
    user <- User.byId(id)
    contactInformation <- user.contactInformation
    email <- contactInformation.email if email.verified
  } yield (user.name, email.address)

  case class User(name: String, contactInformation: Option[ContactInformation], id: Int, canTrack:Boolean)
  object User {
    def byId(id: Int): Option[User] = ???
  }

  object Tracker{
    val store: mutable.Set[Int] = scala.collection.mutable.Set[Int]()
    val GUEST: Int = -1

    def track(id: Int): Unit = store.add(id)
  }

  import Tracker.GUEST

  /**
    Let us start with a simple method: tracking events on a webpage. The only distinctive characteristic here is that whether we have a user
    logged in to the site, we record their ID.
    */
  def track(user: Option[User]): Unit = user match {
    case Some(u) => Tracker.track(u.id)
    case None => Tracker.track(GUEST)
  }
  def trackBetter(user:Option[User]): Unit = Tracker.track(user map (_.id) getOrElse GUEST)

  /**
    Now, due to privacy concerns, we have to allow users to opt out of tracking. Easy-peasy:
    */
  def track2(user: Option[User]): Unit = user match {
    case Some(u) if u.canTrack => Tracker.track(u.id)
    case _ => Tracker.track(GUEST)
  }
  def track2Better(user: Option[User]): Unit = Tracker.track(user filter (_.canTrack) map (_.id) getOrElse GUEST)

  /**
    All was going well, until one day we got a call from our lawyers: if a user opts out, they should never be tracked at all, not even anonymously.
    Hence, our final version:
    */
  def track3(user: Option[User]): Unit = user match {
    case Some(u) if u.canTrack => Tracker.track(u.id)
    case None => Tracker.track(GUEST)
    case _ => // do not track
  }
  def track3Better(user: Option[User]): Unit = {
    // using forall, case Some(u) u.canTrack == false will be implemented, and None.forall will return true, so track(GUEST) when None is fine
    if (user forall (_.canTrack))
      Tracker.track(user map (_.id) getOrElse GUEST)
  }
}
