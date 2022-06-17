package demo.domains

import grails.gorm.transactions.Transactional

@Transactional
class DemoService {
    def addItem(String title, String location, String group) {
        new Item(title: title, location: location, group: group).save(failOnError: true)
    }
}
