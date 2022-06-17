package demo.domains

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class BaseSpec extends Specification implements DomainUnitTest<Base> {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == false
    }
}
