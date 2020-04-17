$("li").each(function (index) {
  var hue = index * 255 / 10,
      $this = $(this),
      timeout = 0,
      left = index * 111;
  
  if (index > 4) {
    left += 16;
  }

  $this.css({
    "background-color": "hsl(" + hue + ", 100%, 70%)",
    "left": left + "px"
  });
  
  $this.data("origin", left);
  
  switch (index) {
    case 4:
    case 5:
      timeout = 300;
      break;
    case 3:
    case 6:
      timeout = 500;
      break;
    case 2:
    case 7:
      timeout = 700;
      break;
    case 1:
    case 8:
      timeout = 900;
      break;
    case 0:
    case 9:
      timeout = 1100;
      break;
  }
  setTimeout(function () {
    $this.removeClass("hide");
  }, timeout);
});

$("ul").on("mousemove", function (e) {
  var x_position = e.offsetX;
});

$("ul").on("mouseleave", function () {
  $(this).children().removeClass("fade grow next nextnext prev prevprev");
});

$("li").on("mouseover", function () {
  var $this = $(this);
  $this.addClass("grow").removeClass("fade next nextnext prev prevprev")
  $this.siblings().removeClass("grow next nextnext prev prevprev").addClass("fade");
  
  $this.next().addClass("next");
  $this.next().next().addClass("nextnext");
  
  $this.prev().addClass("prev");
  $this.prev().prev().addClass("prevprev");
});