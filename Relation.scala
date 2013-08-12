package osmapi
/*
This file is part of openstreetmap-api-testsuite

Copyright (c) 2013 Paul Norman, released under the MIT license.

Defines various test cases for relations
*/
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import bootstrap._

object RelationScenarios {
  val relationScn = scenario("Relation tests")
    .group("R tests") {
      group("Header accept tests") {
        exec(
          http("*/*")
            .get("/relation/5001")
            .header("Accept","*/*")
            .check(
              xpath("""/osm""").count.is(1),
              globalChecks.headerCache,
              globalChecks.contentType,
              status.is(200)
            ))
        .exec(
          http("text/*")
            .get("/relation/5001")
            .header("Accept","text/*")
            .check(
              xpath("""/osm""").count.is(1),
              globalChecks.headerCache,
              globalChecks.contentType,
              status.is(200)
            ))
        .exec(
          http("text/xml")
            .get("/relation/5001")
            .header("Accept","text/xml")
            .check(
              xpath("""/osm""").count.is(1),
              globalChecks.headerCache,
              globalChecks.contentType,
              status.is(200)
            ))
        .exec(
          http("invalid *")
            .get("/relation/5001")
            .header("Accept","*")
            .check(
              xpath("""/osm""").count.is(1),
              globalChecks.headerCache,
              globalChecks.contentType,
              status.is(200)))
        .exec(
          http("invalid josm")
            .get("/relation/5001")
            .header("Accept","text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2")
            .check(
              xpath("""/osm""").count.is(1),
              globalChecks.headerCache,
              globalChecks.contentType,
              status.is(200)
            ))
      }
      .group("Syntax tests") {
        exec(
          http("Invalid request")
            .get("/relation/asdf")
            .check(
              globalChecks.headerCache,
              status.not(500),
              status.not(200)
          ))
        .exec(
          http("Large ID")
            .get("/relation/20000000000000000000") //>2^64
            .check(
              globalChecks.headerNoCache,
              status.not(500),
              status.not(200)
          ))
      }
      .group("Overall tests") {
        exec(
          http("osm attributes")
            .get("/relation/5001")
            .check(
              xpath("""/osm/@version""").is("0.6"),
              xpath("""/osm/@copyright""").is("OpenStreetMap and contributors"),
              xpath("""/osm/@attribution""").is("http://www.openstreetmap.org/copyright"),
              xpath("""/osm/@license""").is("http://opendatacommons.org/licenses/odbl/1-0/"),
              xpath("""/*""").count.is(1),
              globalChecks.headerCache,
              globalChecks.contentType,
              status.is(200)
            ))
        .exec(
            http("existing HEAD")
            .head("/relation/5001")
            .check(
              header("Content-Length").not("0"),
              globalChecks.isEmptyResponse,
              globalChecks.contentType,
              globalChecks.headerCache,
              status.is(200)
            ))
        .exec(
            http("missing")
            .get("/relation/5000")
            .check(
              globalChecks.isEmptyResponse,
              header("Content-Length").is("0"),
              globalChecks.headerNoCache,
              status.is(404)
            ))
        .exec(
            http("missing HEAD")
            .head("/relation/5000")
            .check(
              globalChecks.isEmptyResponse,
              header("Content-Length").is("0"),
              globalChecks.headerNoCache,
              status.is(404)
            ))
      }
      .group("Content tests") {
        exec(
          http("node member")
            .get("/relation/5001")
            .check(
              xpath("""/osm/relation[@id="5001"]/member[1]/@role""").is(""),
              xpath("""/osm/relation[@id="5001"]/member[1]/@ref""").is("5001"),
              xpath("""/osm/relation[@id="5001"]/member[1]/@type""").is("node"),
              xpath("""/osm/relation[@id="5001"]/*""").count.is(1),
              xpath("""/osm/relation[@id="5001"]/@version""").is("1"),
              xpath("""/osm/relation[@id="5001"]/@changeset""").is("5001"),
              xpath("""/osm/relation[@id="5001"]/@user""").is("user_5001"),
              xpath("""/osm/relation[@id="5001"]/@uid""").is("5001"),
              xpath("""/osm/relation[@id="5001"]/@visible""").is("true"),
              xpath("""/osm/relation[@id="5001"]/@*""").count.is(7),
              xpath("""/osm/relation[@id="5001"]""").count.is(1),
              xpath("""/osm/*""").count.is(1),
              status.is(200)
            ))
        .exec(
          http("way member")
            .get("/relation/5002")
            .check(
              xpath("""/osm/relation[@id="5002"]/member[1]/@role""").is(""),
              xpath("""/osm/relation[@id="5002"]/member[1]/@ref""").is("5001"),
              xpath("""/osm/relation[@id="5002"]/member[1]/@type""").is("way"),
              xpath("""/osm/relation[@id="5002"]/*""").count.is(1),
              xpath("""/osm/relation[@id="5002"]/@version""").is("1"),
              xpath("""/osm/relation[@id="5002"]/@changeset""").is("5002"),
              xpath("""/osm/relation[@id="5002"]/@user""").is("user_5002"),
              xpath("""/osm/relation[@id="5002"]/@uid""").is("5002"),
              xpath("""/osm/relation[@id="5002"]/@visible""").is("true"),
              xpath("""/osm/relation[@id="5002"]/@*""").count.is(7),
              xpath("""/osm/relation[@id="5002"]""").count.is(1),
              xpath("""/osm/*""").count.is(1),
              status.is(200)
            ))
        .exec(
          http("relation member")
            .get("/relation/5003")
            .check(
              xpath("""/osm/relation[@id="5003"]/member[1]/@role""").is(""),
              xpath("""/osm/relation[@id="5003"]/member[1]/@ref""").is("5001"),
              xpath("""/osm/relation[@id="5003"]/member[1]/@type""").is("relation"),
              xpath("""/osm/relation[@id="5003"]/*""").count.is(1),
              xpath("""/osm/relation[@id="5003"]/@version""").is("1"),
              xpath("""/osm/relation[@id="5003"]/@changeset""").is("5003"),
              xpath("""/osm/relation[@id="5003"]/@user""").is("user_5003"),
              xpath("""/osm/relation[@id="5003"]/@uid""").is("5003"),
              xpath("""/osm/relation[@id="5003"]/@visible""").is("true"),
              xpath("""/osm/relation[@id="5003"]/@*""").count.is(7),
              xpath("""/osm/relation[@id="5003"]""").count.is(1),
              xpath("""/osm/*""").count.is(1),
              status.is(200)
            ))
        .exec(
          http("no members")
            .get("/relation/5004")
            .check(
              xpath("""/osm/relation[@id="5004"]/*""").count.is(0),
              xpath("""/osm/relation[@id="5004"]/@version""").is("1"),
              xpath("""/osm/relation[@id="5004"]/@changeset""").is("5004"),
              xpath("""/osm/relation[@id="5004"]/@user""").is("user_5004"),
              xpath("""/osm/relation[@id="5004"]/@uid""").is("5004"),
              xpath("""/osm/relation[@id="5004"]/@visible""").is("true"),
              xpath("""/osm/relation[@id="5004"]/@*""").count.is(7),
              xpath("""/osm/relation[@id="5004"]""").count.is(1),
              xpath("""/osm/*""").count.is(1),
              status.is(200)
            ))
        .exec(
          http("roles")
            .get("/relation/5006")
            .check(
              xpath("""/osm/relation[@id="5006"]/member[3]/@role""").is("c"),
              xpath("""/osm/relation[@id="5006"]/member[3]/@ref""").is("5003"),
              xpath("""/osm/relation[@id="5006"]/member[3]/@type""").is("node"),
              xpath("""/osm/relation[@id="5006"]/member[2]/@role""").is("b"),
              xpath("""/osm/relation[@id="5006"]/member[2]/@ref""").is("5002"),
              xpath("""/osm/relation[@id="5006"]/member[2]/@type""").is("node"),
              xpath("""/osm/relation[@id="5006"]/member[1]/@role""").is("a"),
              xpath("""/osm/relation[@id="5006"]/member[1]/@ref""").is("5001"),
              xpath("""/osm/relation[@id="5006"]/member[1]/@type""").is("node"),
              xpath("""/osm/relation[@id="5006"]/*""").count.is(3),
              xpath("""/osm/relation[@id="5006"]/@version""").is("1"),
              xpath("""/osm/relation[@id="5006"]/@changeset""").is("5006"),
              xpath("""/osm/relation[@id="5006"]/@user""").is("user_5006"),
              xpath("""/osm/relation[@id="5006"]/@uid""").is("5006"),
              xpath("""/osm/relation[@id="5006"]/@visible""").is("true"),
              xpath("""/osm/relation[@id="5006"]/@*""").count.is(7),
              xpath("""/osm/relation[@id="5006"]""").count.is(1),
              xpath("""/osm/*""").count.is(1),
              status.is(200)
            ))
        .exec(
          http("roles alternate")
            .get("/relation/5007")
            .check(
              xpath("""/osm/relation[@id="5007"]/member[3]/@role""").is("c"),
              xpath("""/osm/relation[@id="5007"]/member[3]/@ref""").is("5003"),
              xpath("""/osm/relation[@id="5007"]/member[3]/@type""").is("node"),
              xpath("""/osm/relation[@id="5007"]/member[2]/@role""").is("a"),
              xpath("""/osm/relation[@id="5007"]/member[2]/@ref""").is("5001"),
              xpath("""/osm/relation[@id="5007"]/member[2]/@type""").is("node"),
              xpath("""/osm/relation[@id="5007"]/member[1]/@role""").is("b"),
              xpath("""/osm/relation[@id="5007"]/member[1]/@ref""").is("5002"),
              xpath("""/osm/relation[@id="5007"]/member[1]/@type""").is("node"),
              xpath("""/osm/relation[@id="5007"]/*""").count.is(3),
              xpath("""/osm/relation[@id="5007"]/@version""").is("1"),
              xpath("""/osm/relation[@id="5007"]/@changeset""").is("5007"),
              xpath("""/osm/relation[@id="5007"]/@user""").is("user_5007"),
              xpath("""/osm/relation[@id="5007"]/@uid""").is("5007"),
              xpath("""/osm/relation[@id="5007"]/@visible""").is("true"),
              xpath("""/osm/relation[@id="5007"]/@*""").count.is(7),
              xpath("""/osm/relation[@id="5007"]""").count.is(1),
              xpath("""/osm/*""").count.is(1),
              status.is(200)
            ))
        .exec(
          http("anonymous")
            .get("/relation/5008")
            .check(
              xpath("""/osm/relation[@id="5008"]/member[1]/@ref""").is("5001"),
              xpath("""/osm/relation[@id="5008"]/member[1]/@type""").is("node"),
              xpath("""/osm/relation[@id="5008"]/*""").count.is(1),
              xpath("""/osm/relation[@id="5008"]/@version""").is("1"),
              xpath("""/osm/relation[@id="5008"]/@changeset""").is("5008"),
              xpath("""/osm/relation[@id="5008"]/@visible""").is("true"),
              xpath("""/osm/relation[@id="5008"]/@*""").count.is(5),
              xpath("""/osm/relation[@id="5008"]""").count.is(1),
              xpath("""/osm/*""").count.is(1),
              status.is(200)
            ))
        .exec(
          http("tagged")
            .get("/relation/5009")
            .check(
              xpath("""/osm/relation[@id="5009"]/tag[@k="a"]/@v""").is("1"),
              xpath("""/osm/relation[@id="5009"]/member[1]/@ref""").is("5001"),
              xpath("""/osm/relation[@id="5009"]/member[1]/@type""").is("node"),
              xpath("""/osm/relation[@id="5009"]/*""").count.is(2),
              xpath("""/osm/relation[@id="5009"]/@version""").is("1"),
              xpath("""/osm/relation[@id="5009"]/@changeset""").is("5009"),
              xpath("""/osm/relation[@id="5009"]/@user""").is("user_5009"),
              xpath("""/osm/relation[@id="5009"]/@uid""").is("5009"),
              xpath("""/osm/relation[@id="5009"]/@visible""").is("true"),
              xpath("""/osm/relation[@id="5009"]/@*""").count.is(7),
              xpath("""/osm/relation[@id="5009"]""").count.is(1),
              xpath("""/osm/*""").count.is(1),
              status.is(200)
            ))
      }
    }
}