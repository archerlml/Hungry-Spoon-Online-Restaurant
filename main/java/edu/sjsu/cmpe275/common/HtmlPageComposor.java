package edu.sjsu.cmpe275.common;

import java.util.Date;

import edu.sjsu.cmpe275.domain.Order;
import edu.sjsu.cmpe275.domain.User;

public class HtmlPageComposor {

    public static String reservationConfirmation(User user) {
        String page = "<html><body>"
                + "<h3>Dear Customer,</h3>"
                + "<h3>Congratulation! Your account is waiting for your confirmation</h3>"
                + "<h3>Please click the following link to active:</h3>"
                + "<a href='http://52.33.234.249:8080/confirm?id=" + user.getId() + "&code="+user.getCode()+"'>Click me</a>"
                + "<h3>Sincerely</h3>"
                + "<h3>CMPE275 Online order Team</h3>"
                + "</body></html>";
        return page;
    }
    
    public static String orderConfirmation(Order order) {
    	String[] menus = order.getMenuorders().split("A");
    	String menu = "<br />";
    	for (String m : menus) {
    		menu += m + "<br />";
    	}
    	String page = "<html><body>"
                + "<h3>Dear Customer,</h3>"
                + "<h3>Congratulation! Your order is confirmed</h3>"
                + "<h3>Please check the following detail:</h3>"
                + "<h5> Order Menus : " + menu + "</h5>"
                + "<h5> Ready Time : " + order.getReadytime() + "</h5>"
                + "<h5> Pickup Time : " + order.getPickuptime() + "</h5>"
                + "<h5> Total Price : $" + order.getPrice() + "</h5>"
                + "<h3>Sincerely</h3>"
                + "<h3>CMPE275 Online order Team</h3>"
                + "</body></html>";
    	return page;
    }
    
    public static String readyNotification(Date date) {
    	String page = "<html><body>"
                + "<h3>Dear Customer,</h3>"
                + "<h3>Congratulation! Your order is Ready at : "+ date.toString() + "</h3>"
                + "<h3>Please come and pick up your delicious food.</h3>"
                + "<h3>Sincerely</h3>"
                + "<h3>CMPE275 Online order Team</h3>"
                + "</body></html>";
    	return page;
    }
}