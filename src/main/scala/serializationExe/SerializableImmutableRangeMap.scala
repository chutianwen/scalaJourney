/*
package serializationExe

import com.google.common.collect.Multiset.Entry
import org.spark_project.guava.collect.{ImmutableList, RangeMap, RangeSet, TreeRangeMap, TreeRangeSet}

import scala.collection.JavaConversions

object SerializableImmutableRangeMap extends Serializable {

  final class SerializableBuilder[K <: Comparable[_], V]() extends Serializable {
    val keyRanges: RangeSet[K] = TreeRangeSet.create()
    val rangeMap: RangeMap[K, V] = TreeRangeMap.create()

    def put(range: Range[K], value: V): SerializableBuilder[K, V] = {
      checkNotNull(range)
      checkNotNull(value)
      checkArgument(!range.isEmpty(), "Range must not be empty, but was %s", range)
      if (!keyRanges.complement().encloses(range)) {
        // it's an error case; we can afford an expensive lookup
        for (entry: Entry[Range[K], V] <- JavaConversions.asScalaSet(rangeMap.asMapOfRanges().entrySet())) {
          val key: Range[K] = entry.getKey()
          if (key.isConnected(range) && !key.intersection(range).isEmpty()) {
            throw new IllegalArgumentException(
              "Overlapping ranges: range " + range + " overlaps with entry " + entry)
          }
        }
      }
      keyRanges.add(range)
      rangeMap.put(range, value)
      this
    }

    def putAll(rangeMap: RangeMap[K, _ <: V]): SerializableBuilder[K, V] = {
      for (entry <- JavaConversions.asScalaSet(rangeMap.asMapOfRanges().entrySet())) {
        put(entry.getKey(), entry.getValue())
      }
      this
    }

    def build(): SerializableImmutableRangeMap[K, V] ={
      val map: java.util.Map[Range[K], V] = rangeMap.asMapOfRanges()
      val rangesBuilder: ImmutableList.Builder[Range[K]] = new ImmutableList.Builder[Range[K]](map.size())
      val valuesBuilder: ImmutableList.Builder[V] = new ImmutableList.Builder[V](map.size())
      for (entry: Entry[Range[K], V] <- JavaConversions.asScalaSet(map.entrySet())) {
        rangesBuilder.add(entry.getKey())
        valuesBuilder.add(entry.getValue())
      }
      return new SerializableImmutableRangeMap[K, V](rangesBuilder.build(), valuesBuilder.build())
    }
  }

  def builder[K <: Comparable[_], V](): SerializableBuilder[K, V] = {
    new SerializableBuilder[K, V]()
  }
}

class SerializableImmutableRangeMap[K <: Comparable[_], V](ranges: ImmutableList[Range[K]], values: ImmutableList[V]) extends ImmutableRangeMap[K, V](ranges, values) with Serializable {
  def this() {
    this(ImmutableList.of(), ImmutableList.of())
  }
}*/
