# inheritance-and-unique

This repo demonstrates a possible defect in the Grails `UniqueConstraint` validator.

When there is:

* domain class inheritance
* table-per-hierarchy mapping (might not be relevant)
* a property in the **base class**
* a constraint (against that property) in the child
* that constraint inclues `unique` grouped with another property (e.g. `group`) in the **child class**

Then the following exception is generated when attempting to validate:

```
Property [group] is not a valid property of class demo.domains.Base
java.lang.IllegalArgumentException: Property [group] is not a valid property of class demo.domains.Base
	at org.grails.datastore.mapping.reflect.FieldEntityAccess$FieldEntityReflector.getPropertyReader(FieldEntityAccess.java:268)
	at org.grails.datastore.mapping.reflect.FieldEntityAccess$FieldEntityReflector.getProperty(FieldEntityAccess.java:286)
	at org.grails.datastore.gorm.validation.constraints.builtin.UniqueConstraint.processValidate_closure1(UniqueConstraint.groovy:113)
	at org.grails.datastore.gorm.query.criteria.AbstractDetachedCriteria.build(AbstractDetachedCriteria.groovy:821)
	at grails.gorm.DetachedCriteria.build(DetachedCriteria.groovy:585)
	at org.grails.datastore.gorm.validation.constraints.builtin.UniqueConstraint.processValidate(UniqueConstraint.groovy:108)
	at org.grails.datastore.gorm.validation.constraints.AbstractConstraint.validate(AbstractConstraint.java:88)
	at grails.gorm.validation.DefaultConstrainedProperty.validate(DefaultConstrainedProperty.groovy:601)
	at grails.gorm.validation.PersistentEntityValidator.validatePropertyWithConstraint(PersistentEntityValidator.groovy:305)
	at grails.gorm.validation.PersistentEntityValidator.validate(PersistentEntityValidator.groovy:76)
	at org.grails.datastore.gorm.GormValidationApi.doValidate(GormValidationApi.groovy:124)
	at org.grails.datastore.gorm.GormValidationApi.validate(GormValidationApi.groovy:153)
	at org.grails.datastore.gorm.GormValidateable$Trait$Helper.validate(GormValidateable.groovy:71)
	at org.grails.datastore.gorm.GormInstanceApi.doSave(GormInstanceApi.groovy:333)
	at org.grails.datastore.gorm.GormInstanceApi.save_closure5(GormInstanceApi.groovy:180)
	at groovy.lang.Closure.call(Closure.java:405)
	at org.grails.datastore.mapping.core.DatastoreUtils.execute(DatastoreUtils.java:319)
	at org.grails.datastore.gorm.AbstractDatastoreApi.execute(AbstractDatastoreApi.groovy:40)
	at org.grails.datastore.gorm.GormInstanceApi.save(GormInstanceApi.groovy:179)
	at org.grails.datastore.gorm.GormEntity$Trait$Helper.save(GormEntity.groovy:153)
	at demo.domains.DemoService.$tt__addItem(DemoService.groovy:8)
	at demo.domains.DemoService.addItem_closure1(DemoService.groovy)
	at groovy.lang.Closure.call(Closure.java:405)
	at groovy.lang.Closure.call(Closure.java:421)
	at grails.gorm.transactions.GrailsTransactionTemplate$2.doInTransaction(GrailsTransactionTemplate.groovy:94)
	at org.springframework.transaction.support.TransactionTemplate.execute(TransactionTemplate.java:140)
	at grails.gorm.transactions.GrailsTransactionTemplate.execute(GrailsTransactionTemplate.groovy:91)
	at demo.domains.DemoServiceSpec.basic creation works(DemoServiceSpec.groovy:16)
  ```
  
The cause seems to be that when `UniqueConstraint` begins checking the constraint coming from the child class, it finds the constrained property in the base class. It then uses the _base class_ as the context for checking group uniqueness, but as the grouped property is in the child class, it isn't found.
  
The easy workaroud is to swap the properties and move the constraint. So instead of `a(unique: ['b'])`, we do `b(unique: ['a'])`. That would probably work in most cases. But curious if the problem case described above should be made to work.
