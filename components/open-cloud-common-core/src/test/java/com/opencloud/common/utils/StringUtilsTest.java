package com.opencloud.common.utils;


import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;

public class StringUtilsTest {

  @Rule public final ExpectedException thrown = ExpectedException.none();

  // Test written by Diffblue Cover.

  @Test
  public void appendURIParamInputNull0OutputNull() {

    // Arrange
    final String url = null;
    final HashMap<String, String> map = new HashMap<String, String>();

    // Act
    final String actual = StringUtils.appendURIParam(url, map);

    // Assert result
    Assert.assertNull(actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void camelToUnderlineInputNotNullOutputNotNull() {

    // Arrange
    final String param = "\u0002 ";

    // Act
    final String actual = StringUtils.camelToUnderline(param);

    // Assert result
    Assert.assertEquals("", actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void camelToUnderlineInputNullOutputNotNull() {

    // Arrange
    final String param = null;

    // Act
    final String actual = StringUtils.camelToUnderline(param);

    // Assert result
    Assert.assertEquals("", actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void ellipsisInputNullZeroOutputNotNull() {

    // Arrange
    final String str = null;
    final int length = 0;

    // Act
    final String actual = StringUtils.ellipsis(str, length);

    // Assert result
    Assert.assertEquals("", actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void formatBytesInputPositiveOutputNotNull() {

    // Arrange
    final long size = 9L;

    // Act
    final String actual = StringUtils.formatBytes(size);

    // Assert result
    Assert.assertEquals("9Byte", actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void formatBytesInputPositiveOutputNotNull2() {

    // Arrange
    final long size = 8182L;

    // Act
    final String actual = StringUtils.formatBytes(size);

    // Assert result
    Assert.assertEquals("7K", actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void formatBytesInputPositiveOutputNotNull3() {

    // Arrange
    final long size = 1_270_019L;

    // Act
    final String actual = StringUtils.formatBytes(size);

    // Assert result
    Assert.assertEquals("1.0M", actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void formatBytesInputPositiveOutputNotNull4() {

    // Arrange
    final long size = 53_687_285_350L;

    // Act
    final String actual = StringUtils.formatBytes(size);

    // Assert result
    Assert.assertEquals("50.0G", actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void getBankCardCheckCodeInputNotNullOutputN3() {

    // Arrange
    final String nonCheckCodeBankCard = "         ";

    // Act
    final char actual = StringUtils.getBankCardCheckCode(nonCheckCodeBankCard);

    // Assert result
    Assert.assertEquals('N', actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void getBytesInputNotNullOutput1() {

    // Arrange
    final String str = "\u0000";

    // Act
    final byte[] actual = StringUtils.getBytes(str);

    // Assert result
    Assert.assertArrayEquals(new byte[] {(byte)0}, actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void getBytesInputNullOutputNull() {

    // Arrange
    final String str = null;

    // Act
    final byte[] actual = StringUtils.getBytes(str);

    // Assert result
    Assert.assertNull(actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void getExceptionToStringInputNullOutputNotNull() {

    // Arrange
    final Throwable e = null;

    // Act
    final String actual = StringUtils.getExceptionToString(e);

    // Assert result
    Assert.assertEquals("", actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void inStringInputNotNull0OutputFalse() {

    // Arrange
    final String str = "/";
    final String[] strs = {};

    // Act
    final boolean actual = StringUtils.inString(str, strs);

    // Assert result
    Assert.assertFalse(actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void matchBankCardInputNotNullOutputFalse() {

    // Arrange
    final String bankCard = ",";

    // Act
    final boolean actual = StringUtils.matchBankCard(bankCard);

    // Assert result
    Assert.assertFalse(actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void matchBankCardInputNotNullOutputFalse2() {

    // Arrange
    final String bankCard =
        "                              \u803c\u2000\u201e\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f\u803f                \u201f";

    // Act
    final boolean actual = StringUtils.matchBankCard(bankCard);

    // Assert result
    Assert.assertFalse(actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void matchBankCardInputNullOutputFalse() {

    // Arrange
    final String bankCard = null;

    // Act
    final boolean actual = StringUtils.matchBankCard(bankCard);

    // Assert result
    Assert.assertFalse(actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void matchDomainInputNullOutputFalse() {

    // Arrange
    final String domain = null;

    // Act
    final boolean actual = StringUtils.matchDomain(domain);

    // Assert result
    Assert.assertFalse(actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void matchEmailInputNullOutputFalse() {

    // Arrange
    final String email = null;

    // Act
    final boolean actual = StringUtils.matchEmail(email);

    // Assert result
    Assert.assertFalse(actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void matchHttpUrlInputNullOutputFalse() {

    // Arrange
    final String url = null;

    // Act
    final boolean actual = StringUtils.matchHttpUrl(url);

    // Assert result
    Assert.assertFalse(actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void matchIpInputNullOutputFalse() {

    // Arrange
    final String ip = null;

    // Act
    final boolean actual = StringUtils.matchIp(ip);

    // Assert result
    Assert.assertFalse(actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void matchMobileInputNullOutputFalse() {

    // Arrange
    final String mobile = null;

    // Act
    final boolean actual = StringUtils.matchMobile(mobile);

    // Assert result
    Assert.assertFalse(actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void toBooleanInputNullOutputFalse() {

    // Arrange
    final Object val = null;

    // Act
    final Boolean actual = StringUtils.toBoolean(val);

    // Assert result
    Assert.assertFalse(actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void toDoubleInputNullOutputZero() {

    // Arrange
    final Object val = null;

    // Act
    final Double actual = StringUtils.toDouble(val);

    // Assert result
    Assert.assertEquals(0.0, actual, 0.0);
  }

  // Test written by Diffblue Cover.

  @Test
  public void toFloatInputNullOutputZero() {

    // Arrange
    final Object val = null;

    // Act
    final Float actual = StringUtils.toFloat(val);

    // Assert result
    Assert.assertEquals(0.0f, actual, 0.0f);
  }

  // Test written by Diffblue Cover.

  @Test
  public void toIntegerInputNullOutputZero() {

    // Arrange
    final Object val = null;

    // Act
    final Integer actual = StringUtils.toInteger(val);

    // Assert result
    Assert.assertEquals(new Integer(0), actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void toLongInputNullOutputZero() {

    // Arrange
    final Object val = null;

    // Act
    final Long actual = StringUtils.toLong(val);

    // Assert result
    Assert.assertEquals(new Long(0L), actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void toStringInput1OutputNotNull() {

    // Arrange
    final byte[] bytes = {(byte)0};

    // Act
    final String actual = StringUtils.toString(bytes);

    // Assert result
    Assert.assertEquals("\u0000", actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void toStringInputNullNotNullOutputNotNull() {

    // Arrange
    final Object obj = null;
    final String defaultVal = "?";

    // Act
    final String actual = StringUtils.toString(obj, defaultVal);

    // Assert result
    Assert.assertEquals("?", actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void toStringInputPositiveNotNullOutputNotNull() {

    // Arrange
    final Object obj = 2;
    final String defaultVal = ",";

    // Act
    final String actual = StringUtils.toString(obj, defaultVal);

    // Assert result
    Assert.assertEquals("2", actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void underlineToCamelInputNullOutputNotNull() {

    // Arrange
    final String param = null;

    // Act
    final String actual = StringUtils.underlineToCamel(param);

    // Assert result
    Assert.assertEquals("", actual);
  }
}
