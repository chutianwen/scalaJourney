package mock

import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec

object MatchResultObserverTest{
  object Countries{
    val Russia = Country("Russia")
    val Germany = Country("Germany")
  }
  case class Country(name: String)

  type PlayerId = Int
  case class Player(id: PlayerId, nickname: String, country: Country)
  case class MatchResult(winner: PlayerId, loser: PlayerId)

  case class CountryLeaderboardEntry(country: Country, points: Int)

  trait CountryLeaderboard {
    def addVictoryForCountry(country: Country): Unit
    def getTopCountries(): List[CountryLeaderboardEntry]
  }

  class MatchResultObserver(playerDatabase: PlayerDatabase, countryLeaderBoard: CountryLeaderboard) {
    def recordMatchResult(result: MatchResult): Unit = {
      val player = playerDatabase.getPlayerById(result.winner)
      countryLeaderBoard.addVictoryForCountry(player.country)
    }
  }

  trait PlayerDatabase {
    def getPlayerById(playerId: PlayerId): Player
  }
}


class MatchResultObserverTest extends FlatSpec with MockFactory {
  import MatchResultObserverTest._

  val winner = Player(id = 222, nickname = "boris", country = Countries.Russia)
  val loser = Player(id = 333, nickname = "hans", country = Countries.Germany)

  "MatchResultObserver" should "update CountryLeaderBoard after finished match" in {

    // Expectations-First Style (mocks)
    val countryLeaderBoardMock = mock[CountryLeaderboard]

    // Record-then-Verify (stubs). No need to implement a complicated RealPlayerDatabase which contains connection, databases stuff.
    val userDetailsServiceStub = stub[PlayerDatabase]

    // set expectations
    (countryLeaderBoardMock.addVictoryForCountry _).expects(Countries.Russia)

    // configure stubs
    (userDetailsServiceStub.getPlayerById _).when(loser.id).returns(loser)
    (userDetailsServiceStub.getPlayerById _).when(winner.id).returns(winner)

    // run system under test
    val matchResultObserver = new MatchResultObserver(userDetailsServiceStub, countryLeaderBoardMock)
    matchResultObserver.recordMatchResult(MatchResult(winner = winner.id, loser = loser.id))
  }
}