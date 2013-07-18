package osmapi
/*
This file is part of openstreetmap-api-testsuite

Copyright (c) 2013 Paul Norman, released under the MIT license.

Defines various test cases for nodes
*/
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import bootstrap._

object NodeScenario {
  val nodeScn = scenario("Node tests")
    .group("Header tests") {
      exec(
        http("*/*")
          .get("/node/1001")
          .header("Accept","*/*")
          .check(
            status.is(200),
            header("Content-Type").is("text/xml; charset=utf-8"),
            header("Cache-Control").is("max-age=0, private, must-revalidate"),
            xpath("""/osm""").count.is(1)))
      .exec(
        http("text/*")
          .get("/node/1001")
          .header("Accept","text/*")
          .check(
            status.is(200),
            header("Content-Type").is("text/xml; charset=utf-8"),
            header("Cache-Control").is("max-age=0, private, must-revalidate"),
            xpath("""/osm""").count.is(1)))
      .exec(
        http("text/*")
          .get("/node/1001")
          .header("Accept","text/xml")
          .check(
            status.is(200),
            header("Content-Type").is("text/xml; charset=utf-8"),
            header("Cache-Control").is("max-age=0, private, must-revalidate"),
            xpath("""/osm""").count.is(1)))
      .exec(
        http("invalid *")
          .get("/node/1001")
          .header("Accept","*")
          .check(
            status.is(200),
            header("Content-Type").is("text/xml; charset=utf-8"),
            header("Cache-Control").is("max-age=0, private, must-revalidate"),
            xpath("""/osm""").count.is(1)))
      .exec(
        http("invalid josm")
          .get("/node/1001")
          .header("Accept","text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2")
          .check(
            status.is(200),
            header("Content-Type").is("text/xml; charset=utf-8"),
            header("Cache-Control").is("max-age=0, private, must-revalidate"),
            xpath("""/osm""").count.is(1)))
    }
    .group("Overall tests") {
      exec(
        http("osm xml attributes")
          .get("/node/1001")
          .check(
            status.is(200),
            header("Content-Type").is("text/xml; charset=utf-8"),
            header("Cache-Control").is("max-age=0, private, must-revalidate"),
            xpath("""/*""").count.is(1),
            xpath("""/osm/@version""").is("0.6"),
            xpath("""/osm/@copyright""").is("OpenStreetMap and contributors"),
            xpath("""/osm/@attribution""").is("http://www.openstreetmap.org/copyright"),
            xpath("""/osm/@license""").is("http://opendatacommons.org/licenses/odbl/1-0/")
          ))
      .exec(
          http("missing node")
          .get("/node/1000")
          .check(
            status.is(404),
            header("Cache-Control").is("no-cache"),
            header("Content-Length").is("1"),
            sha1.is("b858cb282617fb0956d960215c8e84d1ccf909c6")
          ))
    }
    .group("Content tests") {
      exec(
        http("attributes")
          .get("/node/1001")
          .check(
            xpath("""/osm/*""").count.is(1), 
            xpath("""/osm/node[@id="1001"]""").count.is(1),
            xpath("""/osm/node[@id="1001"]/@*""").count.is(9),
            xpath("""/osm/node[@id="1001"]/@version""").is("1"),
            xpath("""/osm/node[@id="1001"]/@changeset""").is("1001"),
            xpath("""/osm/node[@id="1001"]/@lat""").is("1.001"),
            xpath("""/osm/node[@id="1001"]/@lon""").is("1.001"),
            xpath("""/osm/node[@id="1001"]/@user""").is("user_1001"),
            xpath("""/osm/node[@id="1001"]/@uid""").is("1001"),
            xpath("""/osm/node[@id="1001"]/@visible""").is("true")
          ))
      .exec(
        http("different attributes")
          .get("/node/1002")
          .check(
            xpath("""/osm/*""").count.is(1), 
            xpath("""/osm/node[@id="1002"]""").count.is(1),
            xpath("""/osm/node[@id="1002"]/@*""").count.is(9),
            xpath("""/osm/node[@id="1002"]/@version""").is("2"),
            xpath("""/osm/node[@id="1002"]/@changeset""").is("1002"),
            xpath("""/osm/node[@id="1002"]/@lat""").is("1.002"),
            xpath("""/osm/node[@id="1002"]/@lon""").is("1.001"),
            xpath("""/osm/node[@id="1002"]/@user""").is("user_1002"),
            xpath("""/osm/node[@id="1002"]/@uid""").is("1002"),
            xpath("""/osm/node[@id="1002"]/@visible""").is("true")
          ))
    }
    
}