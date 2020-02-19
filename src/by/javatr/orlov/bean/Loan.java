package by.javatr.orlov.bean;

import java.io.Serializable;
import java.util.Date;


public class Loan implements Serializable {
    private static final long serialVersionUID = -6083127695265804602L;
    private String bookISBN;
    private Date issuedDate;

    public Loan (){
        bookISBN = "bookISBN";
        issuedDate = new Date();
    }

    public Loan (String bookISBN, Date issuedDate){
        this.bookISBN = bookISBN;
        this.issuedDate = issuedDate;
    }

    final public String getBookISBN (){
        return bookISBN;
    }

    final public void setBookISBN (String bookISBN){
        this.bookISBN = bookISBN;
    }

    final public Date getIssuedDate (){
        return issuedDate;
    }

    final public void setIssuedDate (Date issuedDate){
        this.issuedDate = issuedDate;
    }

    @Override
    public boolean equals (Object o){
        if (o == null) return false;
        if (this == o) return true;
        if ((o.getClass() != this.getClass())) return false;
        Loan loan = (Loan) o;
        return bookISBN.equals(loan.getBookISBN()) &&
                issuedDate.equals(loan.getIssuedDate());
    }

    @Override
    public int hashCode (){
        return bookISBN.hashCode() + issuedDate.hashCode();
    }

    @Override
    public String toString (){
        return getClass().getName() + "@" + Integer.toHexString(hashCode()) +
                "{" +
                ", bookISBN=" + bookISBN +
                ", issuedDate=" + issuedDate +
                '}';
    }

}

