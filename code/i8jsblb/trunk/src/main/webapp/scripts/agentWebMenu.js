var jq = jQuery.noConflict();
jq(document).ready(function() {
	function consoleWrite(message) {
		jq('#console').focus().append(message + '\n');
	}

	jq('#demo2').html(jq('#demo1').html());
	jq('#demo1 li').first().addClass('active');
	jq('#demo2 li').first().addClass('active');

	jq('#demo1, #demo2').find("li > a").click(function(e) {
		e.preventDefault();
		var isLink = jq(this).is("a");
		var href = isLink ? jq(this).attr('href') : '';

		if (isLink && href !== '#') {

			//In order to get Page URL and move there(In Money Transfer Category)
			var url = document.URL;
			var path=url.substr(0,url.lastIndexOf('/'));

			window.location.href = path+"/"+href;
			consoleWrite('Click my caret to open my submenu');
		} else if (isLink) {
			consoleWrite('Dummy link');
		}
	});

	consoleWrite('navgoco console waiting for input...');

	jq('pre > code').each(function() {
		var that = jq(this),
			type = that.attr('class'),
			source = that.data('source'),
			code = jq('#' + source + '-' + type).html();
		that.text(jq.trim(code));
	});

	jq(".tabs a").click(function(e) {
		e.preventDefault();
		jq(this).parent().siblings().removeClass('active').end().addClass('active');
		jq(this).parents('ul').next().children().hide().eq(jq(this).parent().index()).show();
	});

	jq(".panes").each(function() {
		jq(this).children().hide().eq(0).show();

	});




	// Initialize navgoco with default options
	jq("#demo1").navgoco({
		caretHtml: '',
		accordion: false,
		openClass: 'open',
		save: true,
		cookie: {
			name: 'navgoco',
			expires: false,
			path: '/'
		},
		slide: {
			duration: 400,
			easing: 'swing'
		},
		// Add Active class to clicked menu item
		onClickAfter: function(e, submenu) {
			e.preventDefault();
			jq('#demo1').find('li').removeClass('active');
			var li =  jq(this).parent();
			var lis = li.parents('li');
			li.addClass('active');
			lis.addClass('active');
		},
	});

	jq("#collapseAll").click(function(e) {
		e.preventDefault();
		jq("#demo1").navgoco('toggle', false);
	});

	jq("#expandAll").click(function(e) {
		e.preventDefault();
		jq("#demo1").navgoco('toggle', true);
	});


});
