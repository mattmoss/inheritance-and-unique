package demo.domains

class Item extends Base {

    String group

    static constraints = {
        title unique: ['group']
    }

}
