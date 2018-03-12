$('#reportbutton').click(
		function() {
			var startDate = $('#start').val();
			var endDate = $('#end').val();

			var sortByStr = 'sort=\"' + $('#sort_by').val() + '\"';
			var orderByStr = 'order=\"' + $('#order_by').val() + "\"";

			var params = '?' + sortByStr + '&' + orderByStr;

			if (startDate != "") {
				params += "&start=" + startDate;
			}
			if (endDate != "") {
				params += "&end=" + endDate;
			}

			var url = "http://52.33.234.249:8080/api/v1/orders" + params;

			/* if(startData > endData) {
					exit();
				} */

			$.getJSON(url, function(data) {
				var items = [];

				// add table head
				var tableHeader = "<tr>" + "<th>OrderId</th>"
				+ "<th>Order-time</th>"
				+ "<th>Fulfillment start-time</th>"
				+ "<th>Ready-time</th>" + "<th>Pickup-time</th>"
				+ "<th>Status</th>" + "<th>Customer-Email</th>"
				+ "<th>Order-Content</th>" + "<th>Price</th>"
				+ "</tr>";
				items.push(tableHeader);

				$.each(data, function(id, order) {
					var tds = "<tr>";
					tds += "<td>" + order.id + "</td>";
					tds += "<td>" + order.ordertime + "</td>";
					tds += "<td>" + order.f_start_time + "</td>";
					tds += "<td>" + order.readytime + "</td>";
					tds += "<td>" + order.pickuptime + "</td>";
					var status = "";
					if (order.status == 1) {
						status = "Queued";
					} else if (order.status == 2) {
						status = "In-Progress";
					} else {
						status = "Fulfilled";
					}
					tds += "<td>" + status + "</td>";

					tds += "<td>" + order.email + "</td>";
					tds += "<td>" + order.menuorders + "</td>";
					tds += "<td>" + order.price + "</td>";
					tds += "</tr>"
						items.push(tds);
				});

				$('#orders_list tbody').html(items);
			})
		});


$('#p_button').click(
		function() {
			var startDate = $('#p_start').val();
			var endDate = $('#p_end').val();
			var params = "";
			if(startDate != "" && endDate != "") {
				params += "?start=" + startDate + "&end=" + endDate;
			}else if (startDate != "") {
				params += "?start=" + startDate;
			}else if (endDate != "") {
				params += "?end=" + endDate;
			}
			
			var url = "http://52.33.234.249:8080/api/v1/p_orders" + params;

			/* if(startData > endData) {
					exit();
				} */

			$.getJSON(url, function(data) {
				var items = [];

				// add table head
				var tableHeader = "<tr>" 
				+ "<th>Category</th>"
				+ "<th>Menu Name</th>"
				+ "<th>Quantity</th>"
				+ "</tr>";
				items.push(tableHeader);

				$.each(data, function(id, order) {
					var tds = "<tr>";
					var category = "";
					if (order.category == 1) {
						category = "Drink";
					} else if (order.category == 2) {
						category = "Appetizer";
					} else if (order.category == 2) {
						category = "Main Course";
					} else {
						category = "Dessert";
					}
					tds += "<td>" + category + "</td>";
					tds += "<td>" + order.menuname + "</td>";
					tds += "<td>" + order.quantity + "</td>";

					tds += "</tr>"
					items.push(tds);
				});

				$('#p_orders_list tbody').html(items);
			})
		});

$('#resetbutton').click(
		function() {
			var url = "http://52.33.234.249:8080/api/v1/reset";

			$.get(url, function(data) {
				alert(data);
			})
		});


function validateForm() {
    var x = document.forms["myForm"]["email"].value;
    var atpos = x.indexOf("@");
    var dotpos = x.lastIndexOf(".");
    if (atpos<1 || dotpos<atpos+2 || dotpos+2>=x.length) {
        alert("Not a valid e-mail address");
        return false;
    }
}