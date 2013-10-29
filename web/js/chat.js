/****************************************************
* Student Name: Choong Teik Tan                     *
* Student Number: 568701                            *
* Student Email: choongt@student.unimelb.edu.au     *
* File: chat.js (SWEN90002 Project 2)               *
****************************************************/

$(document).ready(function(){

    var index = 0;
    var user = "Me";
    var donePull = true;
    var email = $('#email');

// when sent button is clicked
// :: check userName if empty then change userName = "Me".
// :: check chatMessage if empty then unable to send message.
    $("#sent").click(function(){
        user = $('#userName').val();
        var EmptyUserCheck = user.replace(/^\s+|\s+$/, '');
        var message = $('#chatMessage').val();
        var EmptyMessageCheck = message.replace(/^\s+|\s+$/, '');

        if(EmptyUserCheck.length===0) {
            user = "Me";
        }

        if(EmptyMessageCheck.length!=0) {
            // stop interval -> send message to server -> restart interval
            // prevent of blocking event (incase when concurrency - double protection as synchronized)
            self.clearInterval(intervalId);
            while(!donePull){}
            pull(message);
            $('#chatMessage').val('');
            intervalId = self.setInterval(function(){pull("")},1000);
        }

    });

    $("#share").click(function(e){
        e.preventDefault();
        $('#element_to_pop_up').bPopup({
            speed: 450,
            transition: 'slideDown',
            onClose: function(){
                email.val('');
                updateTips('');
                email.removeClass( "ui-state-error" );
            }
        });
    });

    function updateTips( t ) {
        $( ".validateTips" ).text( t ).addClass( "ui-state-highlight" );
        setTimeout(function() {
            $( ".validateTips" ).removeClass( "ui-state-highlight", 1500 );
        }, 500 );
    }

    function checkLength( o, n, min, max ) {
        if ( o.val().length > max || o.val().length < min ) {
            o.addClass( "ui-state-error" );
            updateTips( "Length of " + n + " must be between " + min + " and " + max + "." );
            return false;
        } else {
            email.removeClass( "ui-state-error" );
            return true;
        }
    }

    function checkRegexp( o, regexp, n ) {
      if ( !( regexp.test( o.val() ) ) ) {
        o.addClass( "ui-state-error" );
        updateTips( n );
        return false;
      } else {
        email.removeClass( "ui-state-error" );
        return true;
      }
    }

    $("#sendMail").click(function(e){
        var isValid = true;
        isValid = isValid && checkLength( email, "email", 6, 80 );
        isValid = isValid && checkRegexp( email, /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i, "eg. xxx@student.unimelb.edu.au" );

        if(isValid) {
            share();
            $('#element_to_pop_up').bPopup().close();
        }
        else {}
    });

    function share() {
        var usr = $("#userName").val();
        var EmptyUserCheck = usr.replace(/^\s+|\s+$/, '');
        if(EmptyUserCheck.length===0) {
            usr = "";
        }
        $.getJSON("Chat", {
            "type" : "mail",
            "email" : $("#email").val(),
            "user": usr},
                function(data){
                var status = data.status;
            }
        );
    }

// trigger "#sendMail" button when 'Enter' key is pressed inside "#email"
    $("#email").keyup(function(event){
        if(event.keyCode == 13){
            $("#sendMail").click();
        }
    });

// send message to "Chat" servlet and wait for reply of all new message that not yet display.
// sending empty message to "Chat" servlet mean just to request if there any new message updated.
// returned json :: { "msghead":"2", "newmessage":[{"user":"user-1", "message":"message-1", "timestamp":"time-1"},{"user":"user-2", "message":"message-2", "timestamp":"time-2"}] }
// "msghead" = total number of new message received from servlet.
// "newmessage" = a list of all new message (if msghead == 2 which mean there will have 2 new messages element in "newmessage")
    function pull(msg) {
        if(donePull) {
            donePull = false;
            $.getJSON("Chat", {
                "type" : "pull",
                "user": user,
                "message": msg,
                "msghead" : index},
                    function(data){
                    var totalMsg = data.totalmsg;
                    var newMessage = data.newmessage;

                    for (var i=0; i<totalMsg; i++) {
                        displayMessage(newMessage[i].user, newMessage[i].message, newMessage[i].timestamp);
                    }
                }
            );
            donePull = true;
        }
    }

// append message inside to "#content" div and scroll to the latest updated message.
// also increse index counter (total displayed message).
    function displayMessage(usr, msg, t) {
        $( "<div class=\"bubble\"><p>" + msg + "</p></div>" +
           "<p class=\"center\">" + t + " -- " + usr +"</p>"
        ).appendTo( "#content" );
        index++;
        toBottom();
    }

// trigger "#sent" button when 'Enter' key is pressed inside "#chatMessage"
    $("#chatMessage").keyup(function(event){
        if(event.keyCode == 13){
            $("#sent").click();
        }
    });

// call update content and resize window when first load of index.jsp
    $(window).load(function(){
        //pull("");
    });

//  call when detected user change the size of browser.
    $(window).resize(function(){
        toBottom();
    });

// call update content when detect refresh page
    $(window).unload(function() {
        pull("");
    });

// try to update content for every second.
    var intervalId = self.setInterval(function(){pull("")},1000);
});
