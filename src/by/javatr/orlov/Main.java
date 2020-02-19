package by.javatr.orlov;

import by.javatr.orlov.controller.Controller;

public class Main {

    public static void main (String[] args){

        Controller controller = Controller.getInstance();

//                System.out.println(controller.executeTask("|reset_files|"));


//        System.out.println(controller.executeTask("|log_in|Alarik|1234|"));

//        System.out.println(controller.executeTask("|get_all_users|"));

//        System.out.println(controller.executeTask("|log_in|admin|admin|"));

//        System.out.println(controller.executeTask("|edit_name|ErikErikErikErikErik|"));


//        System.out.println(controller.executeTask("|register_user|name|login|password|"));
//
//        System.out.println(controller.executeTask("|register_user|name1|login1|password1|"));
////
////
//        System.out.println(controller.executeTask("|register_admin|admin|admin|admin|"));
        System.out.println(controller.executeTask("|log_in|admin|admin|"));


//        System.out.println(controller.executeTask("|edit_name|ErikErikErikErikErik|"));


//                System.out.println(controller.executeTask("|get_all_users|"));


//
//        System.out.println(controller.executeTask("|log_in|Alarik|1234|"));
////
//        System.out.println(controller.executeTask("|edit_name|Erik|"));
//        System.out.println(controller.executeTask("|edit_login|Alarik|"));
//        System.out.println(controller.executeTask("|edit_password|1234|"));

//        System.out.println(controller.executeTask("|add_book|1|title|subject|author|"));
//        System.out.println(controller.executeTask("|add_book|2222|title|subject|author|"));
//        System.out.println(controller.executeTask("|add_book|3333|title|subject|author|"));
//        System.out.println(controller.executeTask("|add_book|4444|title|subject|author|"));


//        System.out.println(controller.executeTask("|log_in|login|password|"));
        System.out.println(controller.executeTask("|loan_book|2222|"));
        System.out.println(controller.executeTask("|loan_book|3333|"));
//
//        System.out.println(controller.executeTask("|log_in|Alarik|1234|"));
//
//
//        System.out.println(controller.executeTask("|edit_author|2222|adawd|"));
//        System.out.println(controller.executeTask("|edit_isbn|2222|new isbn|"));
//        System.out.println(controller.executeTask("|edit_subject|1111|new subj|"));
//        System.out.println(controller.executeTask("|edit_title|1111|new title|"));
//
//

//        System.out.println(controller.executeTask("|loan_book|1111|"));
//
//        System.out.println(controller.executeTask("|get_loans|"));


//        System.out.println(controller.executeTask("|log_out|"));
//        System.out.println(controller.executeTask("|log_in|login1|password1|"));

//        System.out.println(controller.executeTask("|return_book|3333|"));
//
//        System.out.println(controller.executeTask("|return_book|2222|"));
////
        System.out.println(controller.executeTask("|get_all_books|"));
//        System.out.println(controller.executeTask("|search_books|subject|"));
//
        System.out.println(controller.executeTask("|get_all_loans|"));
        System.out.println(controller.executeTask("|get_user_loans|admin|"));


//        System.out.println(controller.executeTask("|log_out|"));

    }
}
