package org.hyperledger.fabric.chaincode.entity.validate;

/**
 * 属性校验.
 *
 */
public interface IEntityFieldValidate {

  /**
   * 检查Entity或者BaasController参数是否合法.
   * @param t 参数对象.
   * @param checkBaasId 是否检查BaasId.
   * @throws Exception  校验不通过抛出异常.
   */
  void checkField(Object t, boolean checkBaasId) throws Exception;
}
