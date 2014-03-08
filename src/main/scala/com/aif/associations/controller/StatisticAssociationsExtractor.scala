package com.aif.associations.controller

import com.aif.associations.model.{Item, ConnectionExtractor, AssociationsExtractor}
import javax.validation.constraints.{Max, Min}

object StatisticAssociationsExtractor extends AssociationsExtractor {

  private val NUMERATOR_MULTIPLIER: Double = 2.0

  @Min(value = 0)
  @Max(value = 1)
  def getAssociationsLevel[T <: Item(connectionExtractor: ConnectionExtractor[T, T], item1: T, item2: T): Double = {
    val numerator = calculateNumerator(connectionExtractor, item1, item2)
    val denominator = calculateDenominator(connectionExtractor, item1, item2)
    return numerator / denominator
  }

  private def calculateNumerator[T <: Item](connectionExtractor: ConnectionExtractor[T, T], item1: T, item2: T): Double = {
    val probabilityOfHavingItemsInOneExperiment = connectionExtractor.getProbabilityOfHavingItemsInOneExperiment(item1, item2)
    val aproxWeight = (item1.getWeight + item2.getWeight) / 2.0
    return NUMERATOR_MULTIPLIER * aproxWeight * Math.exp(probabilityOfHavingItemsInOneExperiment)
  }

  private def calculateDenominator[T <: Item](connectionExtractor: ConnectionExtractor[T, T], item1: T, item2: T): Double = {
    val itemsDifficult = (item1.getComplexety + item2.getComplexety) / 2.0
    val aproxInterval = connectionExtractor.getApproximateIntervalBetweenItems(item1, item2)
    return itemsDifficult * aproxInterval
  }

}