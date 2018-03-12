package edu.sjsu.cmpe275.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import edu.sjsu.cmpe275.common.HtmlPageComposor;
import edu.sjsu.cmpe275.domain.ChefSchedule;
import edu.sjsu.cmpe275.domain.MenuItem;
import edu.sjsu.cmpe275.domain.Order;
import edu.sjsu.cmpe275.domain.OrderMenu;
import edu.sjsu.cmpe275.domain.Product;
import edu.sjsu.cmpe275.domain.Rating;
import edu.sjsu.cmpe275.domain.TopPopularity;
import edu.sjsu.cmpe275.domain.User;
import edu.sjsu.cmpe275.service.ChefScheduleService;
import edu.sjsu.cmpe275.service.EmailService;
import edu.sjsu.cmpe275.service.MenuItemService;
import edu.sjsu.cmpe275.service.OrderMenuService;
import edu.sjsu.cmpe275.service.OrderService;
import edu.sjsu.cmpe275.service.ProductService;
import edu.sjsu.cmpe275.service.RatingService;
import edu.sjsu.cmpe275.service.UserService;
import edu.sjsu.cmpe275.util.ChefSched;

@Controller
public class IndexController {

	private ProductService productService;
	private MenuItemService menuItemService;
	private UserService userService;
	private OrderService orderService;
	private EmailService emailService;
	private OrderMenuService orderMenuService;
	private ChefScheduleService chefScheduleService;
	
	@Autowired
	private RatingService ratingService;
	
	@Autowired
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
	@Autowired
	public void setMenuItemService(MenuItemService menuItemService) {
		this.menuItemService = menuItemService;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	
	
	@Autowired
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	
	@Autowired
	public void setMenuOrderService(OrderMenuService orderMenuService) {
		this.orderMenuService = orderMenuService;
	}
	
	@Autowired
	public void setChefScheduleService(ChefScheduleService chefScheduleService) {
		this.chefScheduleService = chefScheduleService;
	}
	
    @RequestMapping("/")
    String index(){
    	scheduling();
        return "index";
    }
    
    @RequestMapping("/login")
    String getLogin(Model model, @RequestParam(value = "error", required=false) String err, HttpServletRequest request){
    	model.addAttribute("user", new User());
        return "login";
    }
    
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    String postLogin(User user, HttpSession session){
    	scheduling();
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    String currentUserName = authentication.getName();
    	    User fetchedUser = userService.findUserByUsername(currentUserName);
    	    session.setAttribute("UserInfo", authentication.getName());
    	    session.setAttribute("UserId", fetchedUser.getId());
//    	    System.out.println(currentUserName);
    	    return "index";
    	}
    	
        return "index";
    }
    
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
    
    @RequestMapping("/register")
    public String getRegister(Model model){
    	model.addAttribute("user", new User());
        return "register";
    }
    
    @RequestMapping(value="/register", method=RequestMethod.POST)
    public String register(User user, String err, Model model) throws NoSuchAlgorithmException{
    	User u_name = userService.findUserByUsername(user.getUsername());

    	if(u_name != null) {
    		model.addAttribute(user);
    		model.addAttribute("err", "Duplicate Username");
    		return "register";
    	}
    	User u_email = userService.findUserByUsername(user.getEmail());
    	if(u_email != null) {
    		model.addAttribute(user);
    		model.addAttribute("err", "Duplicate Email");
    		return "register";
    	}

    	user.setRole("ROLE_waitforverify");
//    	System.out.println(user.getUsername()+user.getPassword()+user.getRole()+user.getEmail()+user.getPhonenumber());
    	model.addAttribute(user);
    	//send email
    	Random r = new Random();
    	int Low = 1000;
    	int High = 9999;
    	int code = r.nextInt(High-Low) + Low;
    	user.setCode(code);
//    	MessageDigest md = MessageDigest.getInstance("MD5");
//    	md.update(user.getPassword().getBytes());
//    	byte[] mdbytes = md.digest();
//    	StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < mdbytes.length; i++) {
//          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
//        }
//        user.setPassword(sb.toString());
    	userService.saveUser(user);
    	String page = HtmlPageComposor.reservationConfirmation(user);
    	emailService.send(user, page);
    	return "login";
    }
    
    @RequestMapping(value="/confirm", method = RequestMethod.GET)
    public String getConfirm (@RequestParam(value = "id") Integer id, @RequestParam(value = "code") Integer code, Model model) {
    	User fetchUser = userService.findUserById(id);
    	if(code.equals(fetchUser.getCode())) {
    		fetchUser.setRole("ROLE_user");
    		userService.saveUser(fetchUser);
    	}
    	model.addAttribute("user", fetchUser);
        return "confirm";
    }
    
    @RequestMapping(value="/profile", method=RequestMethod.POST)
    public String verifyCode(User user){
    	User usertemp = userService.findUserByUsername(user.getUsername());
    	if(usertemp.getCode() == user.getCode()) {
    		user.setRole("ROLE_user");
        	userService.saveUser(user);
    	}

//    	System.out.println(user.getUsername()+user.getPassword()+user.getRole()+user.getEmail()+user.getPhonenumber());
        return "login";
    }
    
    
    @RequestMapping("/admin")
    @PreAuthorize("hasAnyRole('admin')")
    public String admin(Model model){
    	model.addAttribute("product", new Product());
    	Iterable<Order> orders = orderService.listAllOrders();
    	model.addAttribute("orders", orders);
        return "admin";
    }
    
    /**
     * e.g: 
     * '/api/v1/orders?sort="Order-Time"&order="Ascent"&start=2016-05-02&end=2016-05-20'
     * '/api/v1/orders?sort="Fulfillment Start-Time"&order="Descent"&start=2016-05-04&end=2016-05-20'
     * @return
     * @throws ParseException 
     */
    @RequestMapping("/api/v1/orders")
    public ResponseEntity<List<Order>> getAllOrders(
    		@RequestParam(value = "sort") String sortBy, 
    		@RequestParam(value = "order") String orderBy,
    		@RequestParam(value = "start", required = false) String start,
    		@RequestParam(value = "end", required = false) String end) throws ParseException{
    	
    	List<Order> result = new LinkedList<Order>();
    	
    	Date startDate = null, endDate = null;
    	
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	if (start != null) {
    		
			startDate = format.parse(start);
    	}
    	if (end != null) {
			endDate = format.parse(end);
		}
    	
    	Iterable<Order> orders = orderService.listAllOrders();
    	
    	// filter by date
    	for(Order order : orders) {
    		User user = userService.getUserById(order.getUserId());
    		order.setEmail(user.getEmail());
    		Date date = format.parse(order.getOrdertime());
//    		if ("\"Order-Time\"".equals(sortBy)) {
//    			date = format.parse(order.getOrdertime());
//    		} else if ("\"Fulfillment Start-Time\"".equals(sortBy)) {
//    			date = format.parse(order.getF_start_time());
//    		} else {
//    			continue;
//    		}
    	
			if (start != null && end != null && date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
				result.add(order);
				continue;
			}
			
			if (start == null && end == null) {
				result.add(order);
				continue;
			}
			
			if (start != null && date.compareTo(startDate) >= 0 && end == null) {
				result.add(order);
				continue;
			}
			if (end != null && date.compareTo(endDate) <= 0 && start == null) {
				result.add(order);
				continue;
			}
    	}

    	sort(result, sortBy, orderBy);
    	
    	return new ResponseEntity<List<Order>>(result, HttpStatus.OK);
    }
    
    private List<Order> sort(List<Order> orders, String sortBy, String orderBy) {

    	if ("\"Order-Time\"".equals(sortBy)) {
    		Collections.sort(orders, new Comparator<Order>() {
    			@Override
    			public int compare(Order lhs, Order rhs) {
    				Date lDate = null, rDate = null;

    				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    				try {
    					lDate = format.parse(lhs.getOrdertime());
    					rDate = format.parse(rhs.getOrdertime());
    				} catch (ParseException e) {
    					// TODO Auto-generated catch block
//    					e.printStackTrace();
    				}
    				int i = lDate.compareTo(rDate);
    				return i;
    			}
    		});
    	} else if ("\"Fulfillment Start-Time\"".equals(sortBy)) {
    		Collections.sort(orders, new Comparator<Order>() {
    			@Override
    			public int compare(Order lhs, Order rhs) {
    				Date lDate = null, rDate = null;

    				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    				try {
    					lDate = format.parse(lhs.getF_start_time());
    					rDate = format.parse(rhs.getF_start_time());
    				} catch (ParseException e) {
    					// TODO Auto-generated catch block
//    					e.printStackTrace();
    				}
    				int i = lDate.compareTo(rDate);
    				return i;
    			}
    		});
    	}

    	if ("\"Descent\"".equals(orderBy)) {
    		Collections.reverse(orders);
    	}

		return orders;
    	
    }
    
    /**
     * e.g: 
     * '/api/v1/p_orders?start=2016-05-02&end=2016-05-20'
      * @return
     * @throws ParseException 
     */
    @RequestMapping("/api/v1/reset")
    public ResponseEntity<String> resetSystem () {
    	orderMenuService.deleteAll();
    	orderService.deleteAll();
    	return new ResponseEntity<String>("The system has been reset.", HttpStatus.OK);
    }
    
    /**
     * e.g: 
     * '/api/v1/p_orders?start=2016-05-02&end=2016-05-20'
      * @return
     * @throws ParseException 
     */
    @RequestMapping("/api/v1/p_orders")
    public ResponseEntity<List<TopPopularity>> getAllp_Orders(
    		@RequestParam(value = "start", required = false) String start,
    		@RequestParam(value = "end", required = false) String end) throws ParseException{
    	
    	List<TopPopularity> result = new ArrayList<TopPopularity>();
    	HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
    	
    	
    	if(start == null) {
    		start = "1900-01-01";
    	}
    	
    	if(end == null) {
    		end = "9999-01-01";
    	} else {
    		end += " 24:60:60";
    	}
    	
    	Iterable<MenuItem> menuItems = menuItemService.listAllMenuItems();

    	for (MenuItem item : menuItems) {
    		if (map.containsKey(item.getId())) {
    			continue;
    		} else {
    			map.put(item.getId(), 0);
    		}
    	}
    	
    	Iterable<Order> orders = orderService.listAllOrders();
    	
    	// filter by date
    	for(Order order : orders) {
    		if(order.getOrdertime().compareTo(start) < 0 || order.getOrdertime().compareTo(end) > 0) {
    			continue;
    		}
    		for (OrderMenu menu: order.getMenus()) {
    			map.put(menu.getMenuid(), map.get(menu.getMenuid()) + menu.getQuantity());
    		}
    	}
    	
    	for (Entry<Integer, Integer> set : map.entrySet()) {
    		if (set.getValue() <= 0) {
    			continue;
    		}
    		TopPopularity record = new TopPopularity();
    		record.setQuantity(set.getValue());
    		MenuItem m = findMenuItem(set.getKey(), menuItems);
    		record.setCategory(m.getCategory());
    		record.setMenuname(m.getName());
    		result.add(record);
    	}
    	
		Collections.sort(result, new Comparator<TopPopularity>() {
			@Override
			public int compare(TopPopularity lhs, TopPopularity rhs) {
				int i = lhs.getCategory() - rhs.getCategory();
				
				if (i == 0) {
					i = lhs.getQuantity() - rhs.getQuantity();
				}
				return i;
			}
		});

		Collections.reverse(result);
		
    	return new ResponseEntity<List<TopPopularity>>(result, HttpStatus.OK);
    }
    
    private  MenuItem findMenuItem(int id, Iterable<MenuItem> menuItems) {
    	for (MenuItem item: menuItems) {
    		if (id == item.getId()) {
    			return item;
    		}
    	}
    	return null;
    }
    
    @RequestMapping("product/new")
    public String newProduct(Model model){
        model.addAttribute("product", new Product());
        return "productform";
    }
    
    @RequestMapping(value = "product", method = RequestMethod.POST)
    public String saveProduct(Product product){
        productService.saveProduct(product);
        return "redirect:/product/" + product.getId();
    }
    
    @RequestMapping("product/{id}")
    public String showProduct(@PathVariable Integer id, Model model){
        model.addAttribute("product", productService.getProductById(id));
        return "productshow";
    }
    
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("products", productService.listAllProducts());
        return "products";
    }
    
    @RequestMapping("product/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("product", productService.getProductById(id));
        return "productform";
    }
    
    
    @RequestMapping("product/delete/{id}")
    public String delete(@PathVariable Integer id){
        productService.deleteProduct(id);
        return "redirect:/products";
    }
    
    @RequestMapping("menu/new")
    public String newMenuItem(Model model){
        model.addAttribute("menu", new MenuItem());
        return "menuform";
    }
    
    @RequestMapping("menu/edit/{id}")
    public String editMenu(@PathVariable Integer id, Model model){
        model.addAttribute("menu", menuItemService.getMenuItemById(id));
        return "menuform";
    }
    
    @Transactional
    @RequestMapping("menu/delete/{id}")
    public String deleteMenu(@PathVariable Integer id){
//    	try {
//    	ratingService.deleteRatingByMenuItemId(id);
//    	} catch (Exception e) {
//    		
//    	}
    	menuItemService.deleteMenuItem(id);
    	
        return "redirect:/menu";
    }
    
    @RequestMapping(value = "menu", method = RequestMethod.POST)
    public String saveMenuItem(MenuItem menuItem){
    	menuItemService.saveMenuItem(menuItem);
        return "redirect:/menu/" + menuItem.getId();
    }
    
    @RequestMapping("menu/{id}")
    public String showMenuItem(@PathVariable Integer id, Model model){
        model.addAttribute("menu", menuItemService.getMenuItemById(id));
        model.addAttribute("rating", ratingService.getAverageRatingByMenuItemId(id));
        return "menushow";
    }
    
    @RequestMapping(value = "menu", method = RequestMethod.GET)
    public String listCategory(Model model){
        model.addAttribute("menuitems1", menuItemService.getMenuItemByCategory(1));
        model.addAttribute("menuitems2", menuItemService.getMenuItemByCategory(2));
        model.addAttribute("menuitems3", menuItemService.getMenuItemByCategory(3));
        model.addAttribute("menuitems4", menuItemService.getMenuItemByCategory(4));
        model.addAttribute("order", new Order());
        return "orderfood";
    }
    
    @RequestMapping(value = "drink", method = RequestMethod.GET)
    public String drinkCategory(Model model){
        model.addAttribute("menuitems1", menuItemService.getMenuItemByCategory(1));
        return "drink";
    }
    
    @RequestMapping(value = "appetizer", method = RequestMethod.GET)
    public String appetizerCategory(Model model){
        model.addAttribute("menuitems2", menuItemService.getMenuItemByCategory(2));
        return "appetizer";
    }
    
    @RequestMapping(value = "maincourse", method = RequestMethod.GET)
    public String maincourseCategory(Model model){
        model.addAttribute("menuitems3", menuItemService.getMenuItemByCategory(3));
        return "maincourse";
    }
    
    @RequestMapping(value = "dessert", method = RequestMethod.GET)
    public String dessertCategory(Model model){
        model.addAttribute("menuitems4", menuItemService.getMenuItemByCategory(4));
        return "dessert";
    }
    @RequestMapping(value = "farm")
    public String farm(){
    	return "farm";
    }
    
    @RequestMapping(value="test", method = RequestMethod.POST)
    public String test() {
    	return "index";
    }
    
	@RequestMapping(value = "earlytime", method = RequestMethod.GET)
    public String earlyTime(@RequestParam(value = "ptime") String ptime, Model model) {
    	try {
    		ChefSched schedule = chefScheduleService.getEarliestAvailableSchedule(Integer.parseInt(ptime));
//    		System.out.println(schedule.getChefId());
//    		System.out.println(schedule.getStartTime());
//    		System.out.println(schedule.getEndTime());
//    		System.out.println(new Date());
    		model.addAttribute("time", schedule.getEndTime());
    	} catch(Exception e){
//    		System.out.println(e.getMessage());
    	}
    	return "earlytime";
    }
    
	@RequestMapping(value = "checkout", method = RequestMethod.POST)
    public String checkOutOrder(Order order, HttpSession session){
    	
    	try {
    		Calendar cal = Calendar.getInstance();
    		cal.add(Calendar.HOUR, -7);
    		int userid = (int)session.getAttribute("UserId");
    		User user = userService.findUserById(userid);
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date pickup = dateFormatter.parse(order.getPickuptime() + ":00");
//			System.out.println(pickup);
//			Date start = new Date(pickup.getTime());
//			start.setMinutes(start.getMinutes() - order.getPreparetime());
//			System.out.println(start);
//			Schedule schedule = new Schedule(start, pickup);
//			System.out.println(order.getUserId());
//	    	System.out.println(order.getPickuptime());
//	    	System.out.println(order.getMenuOrders());
//	    	System.out.println(order.getPreparetime());
//	    	System.out.println(order.getPrice());
	    	order.setOrdertime(dateFormatter.format(cal.getTime()));
	    	order.setMenuorders(order.getMenuorders().substring(0, order.getMenuorders().length() - 1));
	    	order.setStatus(1);
	    	
	    	ArrayList<Integer> menuId = new ArrayList<>();
	    	ArrayList<Integer> quantity = new ArrayList<>();
	    	String[] menus = order.getMenuorders().split(";");
	    	for (String menu : menus) {
	    		String[] tmp = menu.split(",");
	    		MenuItem cur = menuItemService.getMenuItemByName(tmp[0]);
	    		menuId.add(cur.getId());
	    		quantity.add(Integer.parseInt(tmp[1]));
	    	}
	    	
			ChefSched chefSched = chefScheduleService.getAvailablePreparationSchedule(pickup, order.getPreparetime());
			
			if (pickup.compareTo(cal.getTime()) < 0) {
				return "notAvailable";
			}
			
			if (chefSched != null) {
	    		order.setF_start_time(dateFormatter.format(chefSched.getStartTime()));
	    		Date ready = new Date(chefSched.getStartTime().getTime());
	    		ready.setMinutes(ready.getMinutes() + order.getPreparetime());
	    		order.setReadytime(dateFormatter.format(ready));
	    		order = orderService.saveOrder(order);
	    		ChefSchedule chefSchedule = new ChefSchedule();
	    		chefSchedule.setChefId(chefSched.getChefId());
	    		chefSchedule.setOrderId(order.getId());
	    		chefSchedule.setStartTime(chefSched.getStartTime());
	    		chefSchedule.setEndTime(chefSched.getEndTime());
	    		chefScheduleService.save(chefSchedule);
	    		for (int i = 0; i < menuId.size(); i++) {
	    			OrderMenu ordermenu = new OrderMenu();
	    			ordermenu.setMenuid(menuId.get(i));
	    			ordermenu.setOrderid(order.getId());
	    			ordermenu.setQuantity(quantity.get(i));
	    			orderMenuService.saveOrderMenu(ordermenu);
	    		}
	    		String page = HtmlPageComposor.orderConfirmation(order);
	        	emailService.send(user, page);
			} else {
				return "notAvailable";
			}
	    	
	    	
    	} catch (Exception e) {
//    		System.out.println(e.getMessage());
    	}
    	return "checkout";
    }
    
    @RequestMapping(value = "checkout")
    public String checkOut(Order order){
    	return "checkout";
    }
    
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> uploadFile(
        @RequestParam("uploadfile") MultipartFile uploadfile) {
    	String filename = uploadfile.getOriginalFilename();
      try {
        // Get the filename and build the local file path (be sure that the 
        // application have write permissions on such directory)
        
        String directory = "/home/ec2-user/images/";
//        String directory = "/Users/hongbotian/275-sts/main-bakc/resources/static/images/";
        String filepath = Paths.get(directory, filename).toString();
        // Save the file locally
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
        stream.write(uploadfile.getBytes());
        stream.close();
      }
      catch (Exception e) {
//        System.out.println(e.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      
      return new ResponseEntity<String>(filename, HttpStatus.OK);
    }
    
    @RequestMapping(value = "order", method = RequestMethod.GET)
    public String listOrder(Model model, HttpSession session) throws Exception {
    	Iterable<Order> orders;
    	if (!session.getAttribute("UserInfo").equals("admin")) {
	    	int uid = (int)session.getAttribute("UserId");
	    	orders = orderService.getOrderByUserId(uid);
    	} else {
    		orders = orderService.listAllOrders();
    	}
//    	for (Order o : orders) {
//    		Date now = new Date();
//    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//    		Date pickup = format.parse(o.getPickuptime());
//    		Date start = format.parse(o.getPickuptime());
//    		start.setMinutes(start.getMinutes() - o.getPreparetime());
//			if (pickup.compareTo(now) <= 0) {
//    			o.setReadytime(pickup.toString());
//    			o.setF_start_time(start.toString());
//    			o.setStatus(3);
//    		} else {
//	    		now.setMinutes(now.getMinutes() + o.getPreparetime());
//	    		if (pickup.compareTo(now) <= 0) {
//	    			o.setF_start_time(start.toString());
//	    			o.setStatus(2);
//	    		}
//    		}
//			orderService.saveOrder(o);
//    	}
    	
    	for (Order order : orders) {
    		String menu = order.getMenuorders();
    		String[] menus = menu.split(";");
    		menu = "";
    		for (String m : menus) {
    			menu += m + "<br />";
    		}
    		order.setMenuorders(menu);
    	}
        model.addAttribute("orders", orders);
        return "order";
    }
    
    @RequestMapping("order/cancel/{id}")
    public String deleteOrder(@PathVariable Integer id){
    	orderService.deleteOrder(id);
        return "redirect:/order";
    }
    
    
    @RequestMapping(value = "myratings", method = RequestMethod.GET)
    public String getRating(Model model, HttpSession session) {
    	
    	int userId = (int) session.getAttribute("UserId");
    	
    	List<Order> orders = (List<Order>) orderService.getOrderByUserId(userId);
    	
    	Map<Integer, List<Rating>> ratingsInOrder = new HashMap<>();
    	if (orders != null && orders.size() != 0) {
    		for (Order order : orders) {
    			
    			List<Rating> ratings = ratingService.getRatingsByOrderId(order.getId());
    			
    			
    			if (ratings != null && ratings.size() != 0) {
    				for (Rating rating : ratings) {
    					MenuItem menuItem = menuItemService.getMenuItemById(rating.getMenuItemId());
    					if (menuItem != null) {
    						rating.setMenuItem(menuItem);
    					}
    					
    				}
    				ratingsInOrder.put(order.getId(), ratings);
    				
    			}
    		}
    	}
    	model.addAttribute("myratings", ratingsInOrder);
    	
    	return "rating";
    }
    
    @RequestMapping(value="rating/{orderId}/{menuItemId}/{newrating}", method=RequestMethod.GET)
    public String setRating(@PathVariable("orderId") Integer orderId,
    						@PathVariable("menuItemId") Integer menuItemId,
    						@PathVariable("newrating") Integer newrating) {
    	if (newrating < 0 || newrating > 5) {
    		return "rating";
    	}
    	Rating rating = ratingService.getRatingByOrderIdAndMenuItemId(orderId, menuItemId);
    	if (rating == null || rating.getRated()) {
    		return "rating";
    	}
    	
    	rating.setRated(true);
    	rating.setRating(newrating);
    	ratingService.save(rating);
    	
    	Double newAvarageRating = ratingService.getAverageRatingByMenuItemId(rating.getMenuItemId());
    	
    	MenuItem menuItem = menuItemService.getMenuItemById(rating.getMenuItemId());
    	
    	menuItem.setRating(newAvarageRating);
    	menuItemService.saveMenuItem(menuItem);
    	
    	return "redirect:/myratings";
    }
    
    @Scheduled(fixedDelay=60000)
    public void scheduling() {
    	try {
    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    		Calendar cal = Calendar.getInstance();
    		cal.add(Calendar.HOUR, -7);
    		Date now = cal.getTime();
	    	Iterable<Order> orders = orderService.getOrderByStatus(1);
	    	for (Order order : orders) {
	    		Date fulfill = format.parse(order.getF_start_time());
	    		if (now.compareTo(fulfill) >= 0) {
		    		order.setStatus(2);
		    		orderService.saveOrder(order);
	    		}
	    	}
	    	orders = orderService.getOrderByStatus(2);
	    	for (Order order : orders) {
	    		Date ready = format.parse(order.getReadytime());
	    		if (now.compareTo(ready) >= 0) {
		    		order.setStatus(3);
		    		orderService.saveOrder(order);
		    		User user = userService.getUserById(order.getUserId());
		    		String page = HtmlPageComposor.readyNotification(ready);
		    		emailService.send(user, page);
		    		
		    		
		    		List<OrderMenu> orderMenus = orderMenuService.getOrderMenusByOrderId(order.getId());
		    		
		    		
		    		for (OrderMenu orderMenu : orderMenus) {

		    			if (ratingService.getRatingByOrderIdAndMenuItemId(order.getId(), orderMenu.getMenuid()) == null) {
		    				Rating rating = new Rating();
		    				rating.setOrderId(order.getId());
		    				rating.setMenuItemId(orderMenu.getMenuid());
		    				rating.setRated(false);
		    				ratingService.save(rating);
		    			}
		    		}
	    		}
	    	}
    	} catch (Exception e) {
//    		System.out.println(e.getMessage());
    	}
    }
    
}