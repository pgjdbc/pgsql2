package org.postgresql.adba.communication.packets.parts;

import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import jdk.incubator.sql2.AdbaType;
import jdk.incubator.sql2.SqlType;
import org.postgresql.adba.communication.packets.parsers.BinaryGenerator;
import org.postgresql.adba.pgdatatypes.Box;
import org.postgresql.adba.pgdatatypes.Circle;
import org.postgresql.adba.pgdatatypes.IntegerRange;
import org.postgresql.adba.pgdatatypes.Line;
import org.postgresql.adba.pgdatatypes.LineSegment;
import org.postgresql.adba.pgdatatypes.LocalDateRange;
import org.postgresql.adba.pgdatatypes.LocalDateTimeRange;
import org.postgresql.adba.pgdatatypes.LongRange;
import org.postgresql.adba.pgdatatypes.NumericRange;
import org.postgresql.adba.pgdatatypes.OffsetDateTimeRange;
import org.postgresql.adba.pgdatatypes.Path;
import org.postgresql.adba.pgdatatypes.Point;
import org.postgresql.adba.pgdatatypes.Polygon;

public enum PgAdbaType implements SqlType {
  /**
   * Identifies the generic SQL type {@code BIT}.
   */
  BIT("bit", 1560, AdbaType.BIT, BinaryGenerator::fromBit, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code BIT}.
   */
  BIT_ARRAY("_bit", 1561, AdbaType.ARRAY, BinaryGenerator::fromBitArray, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code TINYINT}.
   */
  TINYINT("tinyint", 21, AdbaType.TINYINT, BinaryGenerator::fromTinyInt, FormatCodeTypes.BINARY),
  /**
   * Identifies the generic SQL type {@code SMALLINT}.
   */
  SMALLINT("smallint", 21, AdbaType.SMALLINT, BinaryGenerator::fromSmallInt, FormatCodeTypes.BINARY),
  /**
   * Identifies the generic SQL type {@code INTEGER}.
   */
  INTEGER("integer", 23, AdbaType.INTEGER, BinaryGenerator::fromInt, FormatCodeTypes.BINARY),
  /**
   * Identifies the generic SQL type {@code BIGINT}.
   */
  BIGINT("bigint", 20, AdbaType.BIGINT, BinaryGenerator::fromBigInt, FormatCodeTypes.BINARY),
  /**
   * Identifies the generic SQL type {@code FLOAT}.
   */
  FLOAT("float", 700, AdbaType.FLOAT, BinaryGenerator::fromFloat, FormatCodeTypes.BINARY),
  /**
   * Identifies the generic SQL type {@code FLOAT}.
   */
  FLOAT_ARRAY("_float4", 1021, AdbaType.FLOAT, BinaryGenerator::fromFloatArray, FormatCodeTypes.BINARY),
  /**
   * Identifies the generic SQL type {@code FLOAT}.
   */
  DOUBLE_ARRAY("_float8", 1022, AdbaType.DOUBLE, BinaryGenerator::fromDoubleArray, FormatCodeTypes.BINARY),
  /**
   * Identifies the generic SQL type {@code REAL}.
   */
  REAL("float", 700, AdbaType.REAL, BinaryGenerator::fromFloat, FormatCodeTypes.BINARY),
  /**
   * Identifies the generic SQL type {@code DOUBLE}.
   */
  DOUBLE("double", 701, AdbaType.DOUBLE, BinaryGenerator::fromDouble, FormatCodeTypes.BINARY),
  /**
   * Identifies the generic SQL type {@code NUMERIC}.
   */
  NUMERIC("numeric", 1700, AdbaType.NUMERIC, BinaryGenerator::fromBigDecimal, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code NUMERIC}.
   */
  NUMERIC_ARRAY("_numeric", 1231, AdbaType.ARRAY, BinaryGenerator::fromBigDecimalArray, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code DECIMAL}.
   */
  DECIMAL("numeric", 1700, AdbaType.DECIMAL, BinaryGenerator::fromBigDecimal, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code CHAR}.
   */
  CHAR("char", 1042, AdbaType.CHAR, BinaryGenerator::fromChar, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code VARCHAR}.
   */
  VARCHAR("varchar", 1043, AdbaType.VARCHAR, BinaryGenerator::fromString, FormatCodeTypes.TEXT),
  /**
   * Identifies the type UUID.
   */
  UUID("uuid", 2950, AdbaType.OTHER, BinaryGenerator::fromUuid, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of UUIDs.
   */
  UUID_ARRAY("_uuid", 2951, AdbaType.ARRAY, BinaryGenerator::fromUuidArray, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code LONGVARCHAR}.
   */
  LONGVARCHAR("text", 25, AdbaType.LONG_VARCHAR, BinaryGenerator::fromString, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code DATE}.
   */
  DATE("date", 1082, AdbaType.DATE, BinaryGenerator::fromLocalDate, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of LocalDate objects.
   */
  DATE_ARRAY("_date", 1182, AdbaType.ARRAY, BinaryGenerator::fromLocalDateArray, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code TIME}.
   */
  TIME("time", 1083, AdbaType.TIME, BinaryGenerator::fromLocalTime, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of LocalTime objects.
   */
  TIME_ARRAY("_time", 1183, AdbaType.ARRAY, BinaryGenerator::fromLocalTimeArray, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code TIMESTAMP}.
   */
  TIMESTAMP("timestamp", 1114, AdbaType.TIMESTAMP, BinaryGenerator::fromLocalDateTime, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of LocalDateTime objects.
   */
  TIMESTAMP_ARRAY("_timestamp", 1115, AdbaType.ARRAY, BinaryGenerator::fromLocalDateTimeArray, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code BINARY}.
   */
  BINARY("bytea", 17, AdbaType.BINARY, BinaryGenerator::fromByteArray, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code VARBINARY}.
   */
  VARBINARY("bytea", 17, AdbaType.VARBINARY, BinaryGenerator::fromByteArray, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code LONGVARBINARY}.
   */
  LONGVARBINARY("bytea", 17, AdbaType.LONG_VARBINARY, BinaryGenerator::fromByteArray, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL value {@code NULL}.
   */
  NULL("void", 0, AdbaType.NULL, BinaryGenerator::fromNull, FormatCodeTypes.TEXT),
  /**
   * Indicates that the SQL type
   * is database-specific and gets mapped to a Java object that can be
   * accessed via the methods getObject and setObject.
   */
  OTHER("null", 0, AdbaType.OTHER, BinaryGenerator::fromString, FormatCodeTypes.BINARY),
  /**
   * Indicates that the SQL type
   * is database-specific and gets mapped to a Java object that can be
   * accessed via the methods getObject and setObject.
   */
  JAVA_OBJECT("java_object", 0, AdbaType.JAVA_OBJECT, BinaryGenerator::fromJavaObject, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code DISTINCT}.
   */
  DISTINCT("distinct", 0, AdbaType.DISTINCT, BinaryGenerator::fromString, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code STRUCT}.
   */
  STRUCT("struct", 0, AdbaType.STRUCT, BinaryGenerator::fromString, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code ARRAY}.
   */
  ARRAY("anyarray", 2277, AdbaType.ARRAY, BinaryGenerator::fromString, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of bytea objects.
   */
  BYTEA_ARRAY("_bytea", 1001, AdbaType.ARRAY, BinaryGenerator::fromByteaArray, FormatCodeTypes.BINARY),
  /**
   * Identifies an array of character objects.
   */
  CHAR_ARRAY("_bpchar", 1014, AdbaType.ARRAY, BinaryGenerator::fromCharArray, FormatCodeTypes.BINARY),
  /**
   * Identifies an array of short objects.
   */
  SHORT_ARRAY("_int4", 1005, AdbaType.ARRAY, BinaryGenerator::fromShortArray, FormatCodeTypes.BINARY),
  /**
   * Identifies an array of integer objects.
   */
  INTEGER_ARRAY("_int4", 1007, AdbaType.ARRAY, BinaryGenerator::fromIntegerArray, FormatCodeTypes.BINARY),
  /**
   * Identifies an array of long objects.
   */
  LONG_ARRAY("_int8", 1016, AdbaType.ARRAY, BinaryGenerator::fromLongArray, FormatCodeTypes.BINARY),
  /**
   * Identifies an array of boolean objects.
   */
  BOOLEAN_ARRAY("_bool", 1000, AdbaType.ARRAY, BinaryGenerator::fromBooleanArray, FormatCodeTypes.BINARY),
  /**
   * Identifies an array of string objects.
   */
  STRING_ARRAY("_varchar", 1015, AdbaType.ARRAY, BinaryGenerator::fromStringArray, FormatCodeTypes.BINARY),
  /**
   * Identifies the generic SQL type {@code BLOB}.
   */
  BLOB("bytea", 17, AdbaType.BLOB, BinaryGenerator::fromByteArray, FormatCodeTypes.BINARY),
  /**
   * Identifies the generic SQL type {@code CLOB}.
   */
  CLOB("text", 25, AdbaType.CLOB, BinaryGenerator::fromString, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code REF}.
   */
  REF("ref", 0, AdbaType.REF, BinaryGenerator::fromNull, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code DATALINK}.
   */
  DATALINK("datalink", 0, AdbaType.DATALINK, BinaryGenerator::fromNull, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code BOOLEAN}.
   */
  BOOLEAN("boolean", 16, AdbaType.BOOLEAN, BinaryGenerator::fromBoolean, FormatCodeTypes.BINARY),

  /**
   * Identifies the SQL type {@code ROWID}.
   */
  ROWID("rowid", 0, AdbaType.ROWID, BinaryGenerator::fromNull, FormatCodeTypes.BINARY),
  /**
   * Identifies the generic SQL type {@code NCHAR}.
   */
  NCHAR("char", 18, AdbaType.NCHAR, BinaryGenerator::fromString, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code NVARCHAR}.
   */
  NVARCHAR("varchar", 1043, AdbaType.NVARCHAR, BinaryGenerator::fromString, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code LONGNVARCHAR}.
   */
  LONGNVARCHAR("text", 25, AdbaType.LONG_NVARCHAR, BinaryGenerator::fromString, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code NCLOB}.
   */
  NCLOB("text", 25, AdbaType.NCLOB, BinaryGenerator::fromString, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code SQLXML}.
   */
  SQLXML("xml", 142, AdbaType.SQLXML, BinaryGenerator::fromString, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code SQLXML}.
   */
  SQLXML_ARRAY("xml[]", 143, AdbaType.ARRAY, BinaryGenerator::fromXmlArray, FormatCodeTypes.BINARY),

  /**
   * Identifies the generic SQL type {@code REF CURSOR}.
   */
  REF_CURSOR("refcursor", 1790, AdbaType.REF_CURSOR, BinaryGenerator::fromNull, FormatCodeTypes.BINARY),

  /**
   * Identifies the generic SQL type {@code TIME WITH TIME ZONE}.
   */
  TIME_WITH_TIME_ZONE("time with timezone", 1266, AdbaType.TIME_WITH_TIME_ZONE, BinaryGenerator::fromOffsetTime,
      FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code TIME WITH TIME ZONE}.
   */
  TIME_WITH_TIME_ZONE_ARRAY("time with timezone", 1270, AdbaType.ARRAY, BinaryGenerator::fromOffsetTimeArray,
      FormatCodeTypes.TEXT),

  /**
   * Identifies the generic SQL type {@code TIMESTAMP WITH TIME ZONE}.
   */
  TIMESTAMP_WITH_TIME_ZONE("timestamp with timezone", 1184, AdbaType.TIMESTAMP_WITH_TIME_ZONE,
      BinaryGenerator::fromOffsetDateTime, FormatCodeTypes.TEXT),
  /**
   * Identifies the generic SQL type {@code TIMESTAMP WITH TIME ZONE}.
   */
  TIMESTAMP_WITH_TIME_ZONE_ARRAY("timestamp with timezone[]", 1185, AdbaType.ARRAY,
      BinaryGenerator::fromOffsetDateTimeArray, FormatCodeTypes.TEXT),
  /**
   * Identifies an Interval of time.
   */
  INTERVAL("interval", 1186, AdbaType.OTHER,
      BinaryGenerator::fromInterval, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of Intervals of time.
   */
  INTERVAL_ARRAY("interval[]", 1187, AdbaType.OTHER,
      BinaryGenerator::fromIntervalArray, FormatCodeTypes.TEXT),
  /**
   * Identifies a String of json.
   */
  JSON("json", 114, AdbaType.OTHER,
      BinaryGenerator::fromJson, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of Strings of json.
   */
  JSON_ARRAY("json[]", 199, AdbaType.OTHER,
      BinaryGenerator::fromJsonArray, FormatCodeTypes.BINARY),
  /**
   * Identifies a String of json.
   */
  JSONB("jsonb", 3802, AdbaType.OTHER,
      BinaryGenerator::fromJson, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of Strings of json.
   */
  JSONB_ARRAY("jsonb[]", 3807, AdbaType.OTHER,
      BinaryGenerator::fromJsonbArray, FormatCodeTypes.BINARY),
  /**
   * Identifies an internet address.
   */
  CIDR("cidr", 650, AdbaType.OTHER,
      BinaryGenerator::fromCidr, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of internet addresses.
   */
  CIDR_ARRAY("cidr", 651, AdbaType.OTHER,
      BinaryGenerator::fromCidrArray, FormatCodeTypes.TEXT),
  /**
   * Identifies a point in a 2-d plane.
   */
  POINT("point", 600, AdbaType.OTHER,
      BinaryGenerator::fromPoint, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of points in a 2-d plane.
   */
  POINT_ARRAY("point[]", 1017, AdbaType.OTHER,
      BinaryGenerator::fromPointArray, FormatCodeTypes.TEXT),
  /**
   * Identifies an infinite line in a 2-d plane.
   */
  LINE("line", 628, AdbaType.OTHER,
      BinaryGenerator::fromLine, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of infinite lines in a 2-d plane.
   */
  LINE_ARRAY("line[]", 629, AdbaType.OTHER,
      BinaryGenerator::fromLineArray, FormatCodeTypes.TEXT),
  /**
   * Identifies a line in a 2-d plane.
   */
  LINE_SEGMENT("lseg", 601, AdbaType.OTHER,
      BinaryGenerator::fromLineSegment, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of lines in a 2-d plane.
   */
  LINE_SEGMENT_ARRAY("lseg[]", 1018, AdbaType.OTHER,
      BinaryGenerator::fromLineSegmentArray, FormatCodeTypes.TEXT),
  /**
   * Identifies a box in a 2-d plane.
   */
  BOX("box", 603, AdbaType.OTHER,
      BinaryGenerator::fromBox, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of boxes in a 2-d plane.
   */
  BOX_ARRAY("box[]", 1020, AdbaType.OTHER,
      BinaryGenerator::fromBoxArray, FormatCodeTypes.TEXT),
  /**
   * Identifies a path of points in a 2-d plane.
   */
  PATH("path", 602, AdbaType.OTHER,
      BinaryGenerator::fromPath, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of paths in a 2-d plane.
   */
  PATH_ARRAY("path[]", 1019, AdbaType.OTHER,
      BinaryGenerator::fromPathArray, FormatCodeTypes.TEXT),
  /**
   * Identifies a polygon in a 2-d plane.
   */
  POLYGON("polygon", 604, AdbaType.OTHER,
      BinaryGenerator::fromPolygon, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of polygons in a 2-d plane.
   */
  POLYGON_ARRAY("path[]", 1027, AdbaType.OTHER,
      BinaryGenerator::fromPolygonArray, FormatCodeTypes.TEXT),
  /**
   * Identifies a circle in a 2-d plane.
   */
  CIRCLE("circle", 718, AdbaType.OTHER,
      BinaryGenerator::fromCircle, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of circles in a 2-d plane.
   */
  CIRCLE_ARRAY("circle[]", 719, AdbaType.OTHER,
      BinaryGenerator::fromCircleArray, FormatCodeTypes.TEXT),
  /**
   * Identifies an oid, what the database uses internally for id columns.
   */
  OID("oid", 26, AdbaType.OTHER,
      BinaryGenerator::fromOid, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of oid, what the database uses internally for id columns.
   */
  OID_ARRAY("oid[]", 1028, AdbaType.OTHER,
      BinaryGenerator::fromOidArray, FormatCodeTypes.TEXT),
  /**
   * Identifies an int8range.
   */
  LONG_RANGE("int8range", 3926, AdbaType.OTHER,
      BinaryGenerator::fromLongRange, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of int8range.
   */
  LONG_RANGE_ARRAY("int8range[]", 3927, AdbaType.OTHER,
      BinaryGenerator::fromLongRangeArray, FormatCodeTypes.TEXT),
  /**
   * Identifies an int4range.
   */
  INTEGER_RANGE("int4range", 3904, AdbaType.OTHER,
      BinaryGenerator::fromIntegerRange, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of int4range.
   */
  INTEGER_RANGE_ARRAY("int4range[]", 3905, AdbaType.OTHER,
      BinaryGenerator::fromIntegerRangeArray, FormatCodeTypes.TEXT),
  /**
   * Identifies an numrange.
   */
  NUMERIC_RANGE("numrange", 3906, AdbaType.OTHER,
      BinaryGenerator::fromNumericRange, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of numrange.
   */
  NUMERIC_RANGE_ARRAY("numrange[]", 3907, AdbaType.OTHER,
      BinaryGenerator::fromNumericRangeArray, FormatCodeTypes.TEXT),
  /**
   * Identifies an tsrange.
   */
  LOCAL_DATE_TIME_RANGE("tsrange", 3908, AdbaType.OTHER,
      BinaryGenerator::fromLocalDateTimeRange, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of tsrange.
   */
  LOCAL_DATE_TIME_RANGE_ARRAY("tsrange[]", 3909, AdbaType.OTHER,
      BinaryGenerator::fromLocalDateTimeRangeArray, FormatCodeTypes.TEXT),
  /**
   * Identifies an tstzrange.
   */
  OFFSET_DATE_TIME_RANGE("tstzrange", 3910, AdbaType.OTHER,
      BinaryGenerator::fromOffsetDateTimeRange, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of tstzrange.
   */
  OFFSET_DATE_TIME_RANGE_ARRAY("tstzrange[]", 3911, AdbaType.OTHER,
      BinaryGenerator::fromOffsetDateTimeRangeArray, FormatCodeTypes.TEXT),
  /**
   * Identifies an daterange.
   */
  LOCAL_DATE_RANGE("daterange", 3912, AdbaType.OTHER,
      BinaryGenerator::fromLocalDateRange, FormatCodeTypes.TEXT),
  /**
   * Identifies an array of tstzrange.
   */
  LOCAL_DATE_RANGE_ARRAY("daterange[]", 3913, AdbaType.OTHER,
      BinaryGenerator::fromLocalDateRangeArray, FormatCodeTypes.TEXT);

  private String name;
  private Integer oid;
  private AdbaType adbaType;
  private Function<Object, byte[]> byteGenerator;
  private FormatCodeTypes formatCodeTypes;

  private static final Map<Class, PgAdbaType> classToDb = new HashMap<>();

  static {
    //classToDb.put(Void.class, BIT);
    classToDb.put(Byte.class, TINYINT);
    classToDb.put(Short.class, SMALLINT);
    classToDb.put(Integer.class, INTEGER);
    classToDb.put(Long.class, BIGINT);
    classToDb.put(Float.class, FLOAT);
    //classToDb.put(.class, REAL);
    classToDb.put(Double.class, DOUBLE);
    classToDb.put(BigDecimal.class, NUMERIC);
    //classToDb.put(.class, DECIMAL);
    classToDb.put(Character.class, CHAR);
    classToDb.put(String.class, VARCHAR);
    //classToDb.put(.class, LONGVARCHAR);
    classToDb.put(LocalDate.class, DATE);
    classToDb.put(LocalTime.class, TIME);
    classToDb.put(LocalDateTime.class, TIMESTAMP);
    //classToDb.put(.class, BINARY);
    //classToDb.put(.class, VARBINARY);
    //classToDb.put(.class, LONGVARBINARY);
    classToDb.put(Void.class, NULL);
    //classToDb.put(.class, OTHER);
    //classToDb.put(.class, JAVA_OBJECT);
    //classToDb.put(.class, DISTINCT);
    //classToDb.put(.class, STRUCT);
    //classToDb.put(.class, ARRAY);
    classToDb.put(byte[].class, BLOB);
    classToDb.put(byte[][].class, BYTEA_ARRAY);
    classToDb.put(Short[].class, SHORT_ARRAY);
    classToDb.put(Integer[].class, INTEGER_ARRAY);
    classToDb.put(Long[].class, LONG_ARRAY);
    classToDb.put(Float[].class, FLOAT_ARRAY);
    classToDb.put(Double[].class, DOUBLE_ARRAY);
    classToDb.put(Boolean[].class, BOOLEAN_ARRAY);
    classToDb.put(Character[].class, CHAR_ARRAY);
    classToDb.put(String[].class, STRING_ARRAY);
    classToDb.put(LocalDate[].class, DATE_ARRAY);
    classToDb.put(LocalTime[].class, TIME_ARRAY);
    classToDb.put(LocalDateTime[].class, TIMESTAMP_ARRAY);
    classToDb.put(OffsetDateTime[].class, TIMESTAMP_WITH_TIME_ZONE_ARRAY);
    classToDb.put(OffsetTime[].class, TIME_WITH_TIME_ZONE_ARRAY);
    classToDb.put(BigDecimal[].class, NUMERIC_ARRAY);
    classToDb.put(char[].class, CLOB);
    //classToDb.put(.class, REF);
    //classToDb.put(.class, DATALINK);
    classToDb.put(Boolean.class, BOOLEAN);
    classToDb.put(java.util.UUID.class, UUID);
    classToDb.put(java.util.UUID[].class, UUID_ARRAY);
    //classToDb.put(.class, ROWID);
    //classToDb.put(.class, NCHAR);
    //classToDb.put(.class, NVARCHAR);
    //classToDb.put(.class, LONGNVARCHAR);
    //classToDb.put(.class, NCLOB);
    //classToDb.put(.class, SQLXML);
    //classToDb.put(.class, REF_CURSOR);
    classToDb.put(OffsetTime.class, TIME_WITH_TIME_ZONE);
    classToDb.put(OffsetDateTime.class, TIMESTAMP_WITH_TIME_ZONE);
    classToDb.put(Duration.class, INTERVAL);
    classToDb.put(Duration[].class, INTERVAL_ARRAY);
    classToDb.put(InetAddress.class, CIDR);
    classToDb.put(Inet4Address.class, CIDR);
    classToDb.put(Inet6Address.class, CIDR);
    classToDb.put(InetAddress[].class, CIDR_ARRAY);
    classToDb.put(Inet4Address[].class, CIDR_ARRAY);
    classToDb.put(Inet6Address[].class, CIDR_ARRAY);
    classToDb.put(Point.class, POINT);
    classToDb.put(Point[].class, POINT_ARRAY);
    classToDb.put(Line.class, LINE);
    classToDb.put(Line[].class, LINE_ARRAY);
    classToDb.put(LineSegment.class, LINE_SEGMENT);
    classToDb.put(LineSegment[].class, LINE_SEGMENT_ARRAY);
    classToDb.put(Box.class, BOX);
    classToDb.put(Box[].class, BOX_ARRAY);
    classToDb.put(Path.class, PATH);
    classToDb.put(Path[].class, PATH_ARRAY);
    classToDb.put(Polygon.class, POLYGON);
    classToDb.put(Polygon[].class, POLYGON_ARRAY);
    classToDb.put(Circle.class, CIRCLE);
    classToDb.put(Circle[].class, CIRCLE_ARRAY);
    classToDb.put(LongRange.class, LONG_RANGE);
    classToDb.put(LongRange[].class, LONG_RANGE_ARRAY);
    classToDb.put(IntegerRange.class, INTEGER_RANGE);
    classToDb.put(IntegerRange[].class, INTEGER_RANGE_ARRAY);
    classToDb.put(NumericRange.class, NUMERIC_RANGE);
    classToDb.put(NumericRange[].class, NUMERIC_RANGE_ARRAY);
    classToDb.put(LocalDateTimeRange.class, LOCAL_DATE_TIME_RANGE);
    classToDb.put(LocalDateTimeRange[].class, LOCAL_DATE_TIME_RANGE_ARRAY);
    classToDb.put(OffsetDateTimeRange.class, OFFSET_DATE_TIME_RANGE);
    classToDb.put(OffsetDateTimeRange[].class, OFFSET_DATE_TIME_RANGE_ARRAY);
    classToDb.put(LocalDateRange.class, LOCAL_DATE_RANGE);
    classToDb.put(LocalDateRange[].class, LOCAL_DATE_RANGE_ARRAY);

  }

  PgAdbaType(String name, Integer oid, AdbaType adbaType, Function<Object, byte[]> byteGenerator,
      FormatCodeTypes formatCodeTypes) {
    this.name = name;
    this.oid = oid;
    this.adbaType = adbaType;
    this.byteGenerator = byteGenerator;
    this.formatCodeTypes = formatCodeTypes;
  }

  /**
   * convert AdbaType to PgAdbaType objects.
   * @param type object to convert
   * @return the matching PgAdbaType object
   */
  public static PgAdbaType convert(SqlType type) {
    if (type instanceof PgAdbaType) {
      return (PgAdbaType) type;
    } else if (type instanceof AdbaType) {
      for (PgAdbaType t : values()) {
        if (t.adbaType == type) {
          return t;
        }
      }
    }

    throw new IllegalArgumentException("unknown type " + type);
  }

  /**
   * Guesses what database type we should use for an java class.
   * @param clazz java class to guess for
   * @return database type
   */
  public static PgAdbaType guessTypeFromClass(Class clazz) {
    if (clazz.isArray() && !classToDb.containsKey(clazz)) {
      return ARRAY;
    }

    return classToDb.computeIfAbsent(clazz, c -> OTHER);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getVendor() {
    return "postgresql";
  }

  @Override
  public Class<?> getJavaType() {
    throw new RuntimeException("not implemented yet");
  }

  public Integer getOid() {
    return oid;
  }

  public Function<Object, byte[]> getByteGenerator() {
    return byteGenerator;
  }

  public FormatCodeTypes getFormatCodeTypes() {
    return formatCodeTypes;
  }
}
