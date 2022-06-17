package demo.domains

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class DemoServiceSpec extends Specification
        implements ServiceUnitTest<DemoService>, DataTest {

    def setupSpec() {
        mockDomain Item
    }

    void "basic creation works"() {
        when:
        def item = service.addItem('Azul', 'St. Louis', 'board-gamers')

        then:
        item
        !item.hasErrors()
    }

}
