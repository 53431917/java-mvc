package controller;
import org.hyperledger.fabric.chaincode.annotation.FabricEntity;
import org.hyperledger.fabric.chaincode.annotation.FabricId;


@FabricEntity
public class Student {

    @FabricId
    private String name;
    private int age;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    
    
}
