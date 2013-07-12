// Don't add the toggle value text if the field has a value, but instead add it on change
function showFlashGrowl(text, type) {
  // types info success, notice, warning, error
  $.gritter.add({
    image: '/images/48_' + type + '.png',
    time: 10000,
    title: type,
    text: text
  });
}

function applyToggleValue($field, populateFrom) {
  if ($field.val() == '') {
    $field.toggleVal({
      populateFrom: populateFrom == '' ? 'label' : populateFrom,
      removeLabels: true
    });
  }
  else {
    $field.change(function() {
      if ($(this).val() == "") {
        $field.toggleVal({
          populateFrom: populateFrom == '' ? 'label' : populateFrom,
          removeLabels: true
        });
      }
    });
  }
}

function isEmptyOrDefaultValue(field) {
  return $(field).val() == "" || $(field).val() == $(field).data("defText");
}

/**
 * Get the value of a cookie with the given name.
 *
 * @example $.cookie('the_cookie');
 * @desc Get the value of a cookie.
 *
 * @param String name The name of the cookie.
 * @return The value of the cookie.
 * @type String
 *
 * @name $.cookie
 * @cat Plugins/Cookie
 * @author Klaus Hartl/klaus.hartl@stilbuero.de
 */
jQuery.cookie = function(name, value, options) {
  if (typeof value != 'undefined') { // name and value given, set cookie
    options = options || {};
    if (value === null) {
      value = '';
      options.expires = -1;
    }
    var expires = '';
    if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
      var date;
      if (typeof options.expires == 'number') {
        date = new Date();
        date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
      } else {
        date = options.expires;
      }
      expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
    }
    // CAUTION: Needed to parenthesize options.path and options.domain
    // in the following expressions, otherwise they evaluate to undefined
    // in the packed version for some reason...
    var path = options.path ? '; path=' + (options.path) : '';
    var domain = options.domain ? '; domain=' + (options.domain) : '';
    var secure = options.secure ? '; secure' : '';
    document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
  } else { // only name given, get cookie
    var cookieValue = null;
    if (document.cookie && document.cookie != '') {
      var cookies = document.cookie.split(';');
      for (var i = 0; i < cookies.length; i++) {
        var cookie = jQuery.trim(cookies[i]);
        // Does this cookie string begin with the name we want?
        if (cookie.substring(0, name.length + 1) == (name + '=')) {
          cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
          break;
        }
      }
    }
    return cookieValue;
  }
};

function getCookie(check_name) {
  var a_all_cookies = document.cookie.split(';');
  var a_temp_cookie = '';
  var cookie_name = '';
  var cookie_value = '';
  var b_cookie_found = false;
  for (i = 0; i < a_all_cookies.length; i++)
  {
    a_temp_cookie = a_all_cookies[i].split('=');
    cookie_name = a_temp_cookie[0].replace(/^\s+|\s+$/g, '');
    if (cookie_name == check_name)
    {
      b_cookie_found = true;
      if (a_temp_cookie.length > 1)
      {
        cookie_value = unescape(a_temp_cookie[1].replace(/^\s+|\s+$/g, ''));
      }
      return cookie_value;
      break;
    }
    a_temp_cookie = null;
    cookie_name = '';
  }
  if (!b_cookie_found)
  {
    return null;
  }
}

$.fn.equalHeights = function() {
  var maxHeight = 0;
  $(this).each(function(index){
      var height = $(this).height();
      if (maxHeight < height) {
          maxHeight = height;
      }
 });
 $(this).height(maxHeight);
};

$(document).ready(function() {

  $(".toggleval").toggleVal({ populateFrom: 'label', removeLabels: true });

  $("a.info").toggle(function() {
    var elem = $(this).parent();
    elem.fadeOut("fast", function() {
      elem.find(".vcard").hide();
      elem.find(".bio").show();
      elem.fadeIn("fast");
    });
    return false;
  }, function() {
    var elem = $(this).parent();
    elem.fadeOut("fast", function() {
      elem.find(".bio").hide();
      elem.find(".vcard").show();
      elem.fadeIn("fast");
    });
    return false;
  });

  $("#modules > div, #modules > li").equalHeights();


  // popup map window
  $("a.trigger-popup-map").fancybox({
    'hideOnContentClick': false,
    'type' : 'iframe',
    'width' : 920,
    'height' : 397,
    'scrolling' : 'no'
  });

  $("#my a:not('.logout')").fancybox({
    padding: 0,
    autoDimensions: false,
    width: 480,
    height: 'auto'
  });
  
  
  
  /******************************************/
  /*    LOGIN / SING UP HOVERS              */
  /******************************************/
  
  $("input#login_hover_user_session_login, #new_person_first_name, input#new_person_last_name, input#new_person_user_attributes_email").toggleVal({
    populateFrom: "label",
    removeLabels: true
  });
  
  $("form#new_person_hover input[type=password], form#login_form_hover input[type=password]").each(function() {
    $(this).addClass("toggleField");
    $(this).attr("defText", $(this).parent().find("label").text());
    $(this).hide();
    $(this).parent().append("<input class='toggleField toggleval fakefield' type='text' name='"+$(this).attr("name")+"' value='"+$(this).parent().find("label").text()+"' />");
    $(this).parent().find("label").remove();
  });
  
  $("input.toggleField[type=password]").live("blur", function(){
    if ($(this).val() == "") {
      $(this).hide();
      $(this).parent().find("input[type=text]").show();
    }
  });

  $("input.toggleField[type=text]").live("focus", function(){
    $(this).hide();
    $(this).parent().find("input[type=password]").show();
    $(this).parent().find("input[type=password]").focus();
  });
  
  $("form#new_person_hover, form#login_form_hover").submit(function() {
    
    $(this).find(".fakefield").each(function() {
      $(this).remove();
      $(this).parent().find("input[type=password]").show();
    });

    $(this).find(".toggleval").each(function() {
      if($(this).val() == $(this).data("defText")) {
        $(this).val("");
      }
    });
    
    $(this).find(".toggleField[type=password]").each(function() {
      if($(this).val() == $(this).attr("defText")) {
        $(this).val("");
      }
    });
  });
  
  
});