package org.hyperledger.fabric.chaincode.query.model;


public class BookmarkClause {

    private String name = "BOOKMARK";
    
    private String bookMark;
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookMark() {
        return bookMark;
    }

    public void setBookMark(String bookMark) {
        this.bookMark = bookMark;
    }
    
    public String toString() {
        return " " + name + " " + bookMark;
    }
    
}
