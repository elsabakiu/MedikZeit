//Adapted from http://code.google.com/p/jquery-cascade/
(function($) {
  $.extend($.fn, {
    cascade: function(options) {

      var settings = $.extend({}, $.fn.cascade.defaults, {
        source: options.source,
        cascaded: options.cascaded
      }, options);

      var dependentDdl = $('#' + options.cascaded);

      if (dependentDdl.children().length == 0) {
        dependentDdl.append('<option>' + options.dependentStartingLabel + '</option>');
        if (settings.disableUntilLoaded) {
          dependentDdl.attr('disabled', 'disabled');
        }
      }

      return this.each(function() {
        var sourceDdl = $(this);

        sourceDdl.change(function() {
          var extraParams = {
              timestamp: +new Date()
          };

          $.each(settings.extraParams, function(key, param) {
              extraParams[key] = typeof param == "function" ? param() : param;
          });

          var sourceVal = $(this).val();
          if (sourceVal == settings.sourceNullValue) {
            dependentDdl.empty().append('<option>' + settings.dependentStartingLabel + '</option>').val('').attr('disabled', 'disabled');
            dependentDdl.change();
            if (settings.onCascade != undefined) {
                settings.onCascade();
            }
            return;
          }
          else {
            dependentDdl.empty().attr('disabled', 'disabled').append('<option>' + settings.dependentLoadingLabel + '</option>');
          }

          $.getJSON(options.source.replace(/%/,$(this).val()), extraParams, function(response) {
            dependentDdl.empty().attr('disabled', null);
            if (response.length > 0) {
              $.each(response, function(i, item) {
                dependentDdl.append('<option value=\"' + item.id + '\"' + ('default_kind' in item ? ' selected=\"selected\"' : '') + '>' + item.name + '</option>');
              });
              dependentDdl.change();
            } else {
              dependentDdl.empty()
              .attr('disabled', 'disabled')
              .append('<option>' + settings.dependentNothingFoundLabel + '</option>');
            }
            if (settings.onCascade != undefined) {
                settings.onCascade();
            }
          });
        });
        if (sourceDdl.val() != settings.sourceNullValue && dependentDdl.val() == settings.dependentNullValue) {
          sourceDdl.change();
        }
        else {
          dependentDdl.removeAttr('disabled');
        }
      });
    }
  });

  $.fn.cascade.defaults = {
    sourceStartingLabel: "Select one first",
    sourceNullValue: '',
    dependentNothingFoundLabel: "No elements found",
    dependentStartingLabel: "Select one",
    dependentNullValue: '',
    dependentLoadingLabel: "Loading options",
    disableUntilLoaded: true,
    extraParams: {},
    onCascade: null
  }
})(jQuery);
