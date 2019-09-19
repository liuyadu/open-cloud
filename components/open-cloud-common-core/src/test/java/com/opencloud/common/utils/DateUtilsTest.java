package com.opencloud.common.utils;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;

public class DateUtilsTest {

  @Rule public final ExpectedException thrown = ExpectedException.none();

  // Test written by Diffblue Cover.

  @Test
  public void formatTextFromtodayInputNullOutputNotNull() {

    // Arrange
    final Date createAt = null;

    // Act
    final String actual = DateUtils.formatTextFromtoday(createAt);

    // Assert result
    Assert.assertEquals("", actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void parseDateInputNullOutputNull() {

    // Arrange
    final String str = null;

    // Act
    final Date actual = DateUtils.parseDate(str);

    // Assert result
    Assert.assertNull(actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void parseUnixTimeStampInputPositiveOutputZero() {

    // Arrange
    final long time = 2L;

    // Act
    final long actual = DateUtils.parseUnixTimeStamp(time);

    // Assert result
    Assert.assertEquals(0L, actual);
  }
}
