package com.opencloud.common.utils;

import com.opencloud.common.utils.CurrencyUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;

public class CurrencyUtilsTest {

  @Rule public final ExpectedException thrown = ExpectedException.none();

  @Rule public final Timeout globalTimeout = new Timeout(10000);

  // Test written by Diffblue Cover.

  @Test
  public void zeroFillInputNegativeInfinityOutputNotNull() {

    // Arrange
    final double number = Double.NEGATIVE_INFINITY;

    // Act
    final String actual = CurrencyUtils.zeroFill(number);

    // Assert result
    Assert.assertEquals("-Infinity.00", actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void zeroFillInputZeroOutputNotNull() {

    // Arrange
    final double number = 0.0;

    // Act
    final String actual = CurrencyUtils.zeroFill(number);

    // Assert result
    Assert.assertEquals("0.00", actual);
  }
}
