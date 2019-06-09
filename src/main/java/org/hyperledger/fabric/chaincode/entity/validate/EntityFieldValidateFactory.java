package org.hyperledger.fabric.chaincode.entity.validate;

import org.hyperledger.fabric.chaincode.entity.validate.impl.EntityFieldValidateImpl;

/**
 * Field验证器工厂类。单例模式.
 * @author hechang
 *
 */
public class EntityFieldValidateFactory {

    /**
     * .
     */
    private static EntityFieldValidateFactory entityFieldValidateFactory
                           = new EntityFieldValidateFactory();

    /**
     * .
     */
    private static IEntityFieldValidate iEntityFieldValidate
                           = new EntityFieldValidateImpl();

    /**
     * .
     * @return
     */
    public static EntityFieldValidateFactory getInstance() {
        return entityFieldValidateFactory;
    }

    /**
     * .
     * @return
     */
    public IEntityFieldValidate getEntityFieldValidate(){
        return iEntityFieldValidate;
    }

}
