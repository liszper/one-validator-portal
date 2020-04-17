$(function(){
		 
		    var gridster = $(".gridster").gridster({
		        widget_selector: 'div',
		        widget_margins: [10, 10],
		        widget_base_dimensions: [140, 140],
		        min_cols: 6,
		        resize: {
		            enabled: true
		        }
		    }).data('gridster');
		 
		});