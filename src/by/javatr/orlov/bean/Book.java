package by.javatr.orlov.bean;

import java.io.Serializable;

public class Book implements Serializable, Cloneable {
    private static final long serialVersionUID = -4697934221566276771L;

    private String iSBN;
    private String title;
    private String subject;
    private String author;
    private boolean issued;

    public Book (){
        iSBN = "0000";
        title = "Title";
        subject = "Subject";
        author = "Author";
        issued = false;

    }

    public Book (String iSBN, String title, String subject, String author){
        this.iSBN = iSBN;
        this.title = title;
        this.subject = subject;
        this.author = author;
        this.issued = false;
    }

    public Book (String iSBN, String title, String subject, String author, boolean issued){
    this(iSBN, title, subject, author);
    this.setIssued(issued);
    }


    public Book(Book book){
        this.iSBN = book.getISBN();
        this.title = book.getTitle();
        this.subject = book.getSubject();
        this.author = book.getAuthor();
        this.issued = book.isIssued();
    }

    final public String getTitle (){
        return title;
    }

    final public void setTitle (String title){
        this.title = title;
    }

    final public String getSubject (){
        return subject;
    }

    final public void setSubject (String subject){
        this.subject = subject;
    }

    final public String getAuthor (){
        return author;
    }

    final public void setAuthor (String author){
        this.author = author;
    }

    final public boolean isIssued (){
        return issued;
    }

    final public void setIssued (boolean issued){
        this.issued = issued;
    }

    final public String getISBN (){
        return iSBN;
    }

    final public void setISBN (String iSBN){
        this.iSBN = iSBN;
    }

    @Override
    public boolean equals (Object o){
        if (o == null) return false;
        if (this == o) return true;
        if ((o.getClass() != this.getClass())) return false;
        Book book = (Book) o;
        return iSBN.equals(book.iSBN) &&
                title.equals(book.title) &&
                subject.equals(book.subject) &&
                author.equals(book.author);
    }

    @Override
    public int hashCode (){
        return iSBN.hashCode() + title.hashCode() + subject.hashCode() + author.hashCode();
    }

    @Override
    public String toString (){
        return getClass().getName() + "@" + Integer.toHexString(hashCode()) +
                "{" +
                "ISBN='" + iSBN + '\'' +
                ", title='" + title + '\'' +
                ", subject='" + subject + '\'' +
                ", author='" + author + '\'' +
                ", issued=" + issued +
                '}';
    }

    @Override
    public Book clone () throws CloneNotSupportedException{
        return new Book(this);
    }
}