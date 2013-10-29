/****************************************************
* Student Name: Choong Teik Tan                     *
* Student Number: 568701                            *
* Student Email: choongt@student.unimelb.edu.au     *
* File: proj2-jscript.js (SWEN90002 Project 2)      *
****************************************************/

$(document).ready(function(){
    /*
    $(document).bind('mobileinit', function () {
        $.mobile.minScrollBack = 0;
    });
    */

    $('textarea').autosize();

    window.history.forward();
    function noBack() {window.history.forward();}

    $("#message-contet").keyup(function(event){
        if(event.keyCode == 13){
            $("#message-contet").val("");
            $("#send-message-button").click();
        }
    });

    $("#login-email").keyup(function(event){
        if(event.keyCode == 13){
            $("#login-button").click();
        }
    });

    $("#login-password").keyup(function(event){
        if(event.keyCode == 13){
            $("#login-button").click();
        }
    });

    $("#register-email").keyup(function(event){
        if(event.keyCode == 13){
            $("#register-button").click();
        }
    });

    $("#reminder-email").keyup(function(event){
        if(event.keyCode == 13){
            $("#reminder-button").click();
        }
    });

    $("#login-button").click(function(){
        alert();
        var email_field = $("#login-email");
        var email = $("#login-email").val();
        var password = $("#login-password").val();
        var EmptyPasswordCheck = password.replace(/^\s+|\s+$/, '');

        var isValid = true;
        isValid = isValid && checkLength( email_field, "email", 6, 80, ".validateTips-login", ".tips-to-display-login" );
        isValid = isValid && checkRegexp( email_field, /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i, "Invalid Email Format!\n E.G: [xxx@student.unimelb.edu.au]", ".validateTips-login", ".tips-to-display-login" );

        if(isValid && EmptyPasswordCheck == 0) {
            updateTips( "Password Must Not Be Empty!", ".validateTips-login", ".tips-to-display-login" );
        }
        else if (isValid && EmptyPasswordCheck != 0){
            blocking_ui();
            login(email, password);
        }
    });

    $("#register-button").click(function(){
        var email_field = $("#register-email");
        var email = email_field.val();
        var isValid = true;

        isValid = isValid && checkLength( email_field, "email", 6, 80, ".validateTips-register", ".tips-to-display-register" );
        isValid = isValid && checkRegexp( email_field, /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i, "Invalid Email Format!\n E.G: [xxx@student.unimelb.edu.au]", ".validateTips-register", ".tips-to-display-register" );

        if(isValid) {
            blocking_ui();
            register(email);
        }
    });

    $("#reminder-button").click(function(){
        var email_field = $("#reminder-email");
        var email = email_field.val();
        var isValid = true;
        isValid = isValid && checkLength( email_field, "email", 6, 80, ".validateTips-reminder", ".tips-to-display-reminder" );
        isValid = isValid && checkRegexp( email_field, /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i, "Invalid Email Format!\n E.G: [xxx@student.unimelb.edu.au]", ".validateTips-reminder", ".tips-to-display-reminder" );

        if(isValid) {
            blocking_ui();
            reminder(email);
        }
    });

    $(".logout").click(function(){
        blocking_ui();
        logout();
    });

    $("#confirm-delete-yes").click(function(){
        blocking_ui();

        if($.cookie("email") == null || $.cookie("token") == null) {
            //un_blocking_ui();  // unblocking UI
            $.removeCookie('email');
            $.removeCookie('token');
            showMemberMessage("Error Occur", "No Security Token Found!<br>You Are Forced to Logout..<br>Please Relogin and Try Again..");
            setTimeout(function() {
                un_blocking_ui();   //
                window.location.href="index.jsp";
            }, 5000 );
        }
        else {
            $.ajax({
                type: "POST",
                data: {email: $.cookie("email"), token: $.cookie("token")},
                url: "Delete",
                success: function(data){
                    var deleted = data.deleted;
                    var status = data.status;
                    var title = "Delete Registration Complete";
                    var message = "You Are Now Successfully Delete Registration..<br>You Will Need to Register To Use Our Service..<br>BYE!";

                    if(!deleted) {
                        title = 'Invalid Security Token';
                        message = 'Security Token Is Not Valid!<br>Sorry You Are Now Forced To Logout..<br>Please Re-login And Try Again..';
                    }

                    showMemberMessage(title, message);
                    $.removeCookie('email');
                    $.removeCookie('token');
                    setTimeout(function() {
                        un_blocking_ui();   //
                        window.location.href="index.jsp";
                    }, 5000 );
                },
                error: function () {
                    showMemberMessage("Error Occur", "No Security Token Found!<br>You Are Forced to Logout..<br>Please Relogin and Try Again..");
                    $.removeCookie('email');
                    $.removeCookie('token');
                    setTimeout(function() {
                        un_blocking_ui();   //
                        window.location.href="index.jsp";
                    }, 5000 );
                }
            });
        }
    });

    $("#send-message-button").click(function(){
        var usr = "Me";
        var msg = "Hello";
        var t = "Time";
        displayMessage(usr, msg, t);
    });

    $("#reset").click(function(){
        blocking_ui();  // blocking UI

        if($.cookie("email") == null || $.cookie("token") == null) {
            showMemberMessage("Error Occur", "No Security Token Found!<br>You Are Forced to Logout..<br>Please Relogin and Try Again..");
            $.removeCookie('email');
            $.removeCookie('token');
            setTimeout(function() {
                un_blocking_ui();  // unblocking UI
                window.location.href="index.jsp";
            }, 5000 );
        }
        else {
            $.ajax({
                type: "POST",
                data: {email: $.cookie("email"), token: $.cookie("token")},
                url: "Reset",
                success: function(data){
                    var reset = data.reset;
                    var status = data.status;

                    if(reset) {
                        un_blocking_ui();  // unblocking UI
                        showMemberMessage("Password Reset Success!", "A NEW Password had sent to your Email!"+"<br>"+$.cookie("email"));
                    }
                    else {
                        var title = 'Server Error!';
                        var message = 'Sorry, Could\'t Processes Your Request At The Moment..<br>Please Try Again Later..';

                        if(status === "incorrectToken" || status === "notAMember") {
                            title = 'Invalid Security Token';
                            message = 'Security Token Is Not Valid!<br>Sorry You Are Now Forced To Logout..<br>Please Re-login And Try Again..';

                            showMemberMessage(title, message);
                            $.removeCookie('email');
                            $.removeCookie('token');
                            setTimeout(function() {
                                un_blocking_ui();  // unblocking UI
                                window.location.href="index.jsp";
                            }, 5000 );
                        }
                        else {
                            un_blocking_ui();  // unblocking UI
                            showMemberMessage(title, message);
                        }

                    }
                },
                error: function () {
                    showMemberMessage("Error Occur", "No Security Token Found!<br>You Are Forced to Logout..<br>Please Relogin and Try Again..");
                    $.removeCookie('email');
                    $.removeCookie('token');
                    setTimeout(function() {
                        un_blocking_ui();  // unblocking UI
                        window.location.href="index.jsp";
                    }, 5000 );
                }
            });
        }
    });

    var isCreator = true;
    $("#create-group-button").click(function(){
        var listStart = '<ul data-role="listview" data-inset="true" data-filter="true" data-divider-theme="b" data-theme="c" data-filter-theme="e" data-filter-placeholder="Filter Group">';
        var createdGroup = '<li id="group-created" data-role="list-divider" >Created Group</li>';
        var createdGroupItem = '<li><a href="#">Created 1</a></li>';
        var jointedGroup = '<li id="group-jointed" data-role="list-divider">Jointed Group</li>';
        var jointedGroupItem = '<li><a href="#">Jointed 1</a></li>';
        var listEnd = '</ul>';
        //appendToList(true, '123321', 'MyGroup');

        //var isCreator = true;
        var groupID = '#profile';
        var groupName = 'Not My Group';
        if(isCreator) {
            $('<li data-icon="gear"><a href="'+groupID+'">'+groupName+'</a></li>').insertAfter("#group-created");
            isCreator = false;
        }
        else {
            $('<li><a href="'+groupID+'">'+groupName+'</a></li>').insertAfter("#group-jointed");
            isCreator = true;
        }

        $("#grouplist").listview("refresh");
    });

    function checkLength( o, n, min, max , vt, vtd) {
        if ( o.val().length > max || o.val().length < min ) {
            o.addClass( "ui-state-error" );
            updateTips( "Length of " + n + " must be between " + min + " and " + max + ".", vt, vtd );
            return false;
        } else {
            o.removeClass( "ui-state-error" );
            return true;
        }
    }

    function checkRegexp( o, regexp, n , vt, vtd) {
      if ( !( regexp.test( o.val() ) ) ) {
        o.addClass( "ui-state-error" );
        updateTips( n , vt, vtd);
        return false;
      } else {
        o.removeClass( "ui-state-error" );
        return true;
      }
    }

    function updateTips( t, vt, vtd ) {
        $( vt ).text( t ).addClass( "ui-state-highlight" );
        $( vtd ).show();
        setTimeout(function() {
            $( vt ).removeClass( "ui-state-highlight", 1500 );
            $( vt ).text("")
            $( vtd ).hide();
        }, 5000 );
    }

    function blocking_ui() {
        $('#block-ui').show();
        $.mobile.loading( 'show', {
            text: 'Precessing..',
            textVisible: true,
            theme: 'b',
            html: ""
        });
    }

    function un_blocking_ui() {
        $('#block-ui').hide();
        $.mobile.loading('hide');
    }

    function showServerMessage (title, data) { // display message
        $("#message-body").text("");
        var d = data.split("<br>");
        $("#message-title").text(title);
        for ( var i = 0, l = d.length; i < l; i++ )
            $("#message-body").append( "<center><p>"+d[i]+"</p></center>" );
        $.mobile.changePage( "#message" );//, { transition: "slideup"} );
    }

    function showMemberMessage (title, data) { // display message
        $("#message-body-member").text("");
        var d = data.split("<br>");
        $("#message-title-member").text(title);
        for ( var i = 0, l = d.length; i < l; i++ )
            $("#message-body-member").append( "<center><p>"+d[i]+"</p></center>" );
        $.mobile.changePage( "#message-member" );//, { transition: "slideup"} );
    }

    function displayMessage(usr, msg, t) {
        $( "<div class=\"bubble\"><p>" + msg + "</p></div>" +
           "<p class=\"center\">" + t + " -- " + usr +"</p>"
        ).appendTo( "#message-display" );
        //index++;
        toBottom();
    }

    /* Call To Server Function*/
    function register (emailAdd) {
        $.ajax({
           type: "POST",
            data: {email: emailAdd},
            url: "Register",
            success: function(data){
                un_blocking_ui(); // unblocking UI
                //var dataString ={"serverreply":data.serverreply, "email":data.email, "status":data.status};

                if (data.status === "reregister") {
                    showServerMessage("Registration Error", "Server encountered an error processing registration.<br>Please try again later.");
                }
                else {
                    showServerMessage(data.title, data.serverreply);
                }
            },
            error: function () {
                un_blocking_ui(); // unblocking UI
                showServerMessage("Registration Error", "Error accour.<br>Please try again..");
            }
        });
    }

    function reminder (emailAdd) {
        $.ajax({
            type: "POST",
            data: {email: emailAdd},
            url: "Reminder",
            success: function(data){
                un_blocking_ui(); // unblocking UI
                showServerMessage(data.title, data.serverreply);
            },
            error: function () {
                un_blocking_ui(); // unblocking UI
                showServerMessage("Reminder Error", "Error accour.\nPlease try again..");
            }
        });
    }

    function login(emailAdd, password) {
        $.ajax({
            type: "POST",
            data: {email: emailAdd, password: password},
            url: "Login",
            success: function(data){
                var success = data.success;
                var status = data.status;
                var token = data.token;
                var repliedEmail = data.email;

                un_blocking_ui();  // unblocking UI
                // invalidEmail || notAMember || incorrectPassword || login
                if (success) {
                    // use cookie to store email and token (expire 1 hours later)
                    $.cookie("email", repliedEmail, {expires: 1});
                    $.cookie("token", token, {expires: 1});
                    window.location.href="memberpage.jsp";
                }
                else {
                    var title = '';
                    var message = '';

                    if (status === "invalidEmail") {
                        title = 'Invalid Email';
                        message = 'Invalid Email Detedted!<br>Please Recheck Your Email Field.';
                    }
                    else if(status === "Error") {
                        title = 'Unknow Error Occur';
                        message = 'Please Try Again Later..<br>If it happen again please Report to choongt@student.unimelb.edu.au';
                    }
                    else if ((status === "notAMember") || (status === "incorrectPassword")) {
                        title = 'Incorrect Data Detected!';
                        message = 'Server Detected You Are Trying To Login<br>To Account: ('+repliedEmail+')!<br>If Not Please Recheck Your Email Field AND Makesure You Enter Correct Password.';
                    }
                    // Too Details Information will lead to easy break the system. (So I decide not to specify which details is incorrect)
                    /*
                    else if(status === "notAMember") {
                        title = 'No Such User';
                        message = '<p>Email ('+repliedEmail+') Not Yet Register As Member!<br>Please Recheck Your Email Field Or Register As Member.</p>';
                    }
                    else if(status === "incorrectPassword") {
                        title = 'Incorrect Password';
                        message = '<p>Incorrect Password Detedted for ('+repliedEmail+')<br>Please Try Again Or Request For Password Reminder.</p>';
                    }
                    */

                    showServerMessage(title, message);
                }
            },
            error: function () {
                un_blocking_ui();  // unblocking UI
                showServerMessage("Login Error", "Error accour.<br>Please try again..");
            }
        });
    }

    function logout() {
        if($.cookie("email") == null || $.cookie("token") == null) {
            $.removeCookie('email');
            $.removeCookie('token');
            showServerMessage("Error Occur", "No Security Token Found!<br>You Are Forced to Logout..<br>Please Relogin and Try Again..");

            setTimeout(function() {
                //$.mobile.changePage("../",{ reloadPage: true });
                un_blocking_ui();  // unblocking UI
                window.location.href="index.jsp";
            }, 5000 );
        }
        else {
            $.ajax({
                type: "POST",
                data: {email: $.cookie("email"), token: $.cookie("token")},
                url: "Logout",
                success: function(data){
                    var logout = data.logout;
                    var status = data.status;
                    if(logout) {
                        un_blocking_ui();  // unblocking UI
                        $.removeCookie('email');
                        $.removeCookie('token');
                        window.location.href="index.jsp";
                    }
                    else {
                        var title = '';
                        var message = '';

                        if(status === "incorrectToken") {
                            title = 'Invalid Security Token';
                            message = 'Security Token Is Not Valid!<br>Sorry You Are Now Forced To Logout..<br>Please Re-login And Try Again..';
                        }
                        else {
                            title = "Error Accour!";
                            message = "Sorry You Are Now Forced To Logout..<br>Please Try Re-login And Proceed Request Again..";
                        }

                        $.removeCookie('email');
                        $.removeCookie('token')
                        showServerMessage(title, message);

                        setTimeout(function() {
                            un_blocking_ui();  // unblocking UI
                            window.location.href="index.jsp";
                        }, 5000 );
                    }
                },
                error: function () {
                    $.removeCookie('email');
                    $.removeCookie('token');
                    showServerMessage("Error Occur", "No Security Token Found!<br>You Are Forced to Logout..<br>Please Relogin and Try Again..");

                    setTimeout(function() {
                        un_blocking_ui();  // unblocking UI
                        window.location.href="index.jsp";
                    }, 5000 );
                }
            });
        }
    }
});

// cookie expire
function getExpiresMinutes (minutes) {
    var date = new Date();
    //var minutes = 30; // 30 minutes
    date.setTime(date.getTime() + (minutes * 60 * 1000));
    return date;
}

function toBottom() {
    var objDiv = document.getElementById("content");
    objDiv.scrollTop = objDiv.scrollHeight;
}

/*
    var listCreated = false;

    function appendToList(isCreator, groupID, groupName){
        //$('#grouplist').empty();
        //Create the listview if not created
        if(!listCreated){
            var listStyle = '<ul data-icon="false" id="grouplist" data-role="listview" data-inset="true" data-filter="true" data-divider-theme="b" data-theme="c" data-filter-theme="e" data-filter-placeholder="Filter Group">\n\
                                <li id="group-created" data-role="list-divider">Created Group</li>\n\
                                <li id="group-jointed" data-role="list-divider">Jointed Group</li>\n\
                             </ul>';
            $("#group-list-container").append(listStyle);//'<ul id="grouplist" data-role="listview" data-inset="true" data-filter="true" data-divider-theme="b" data-theme="c" data-filter-theme="e" data-filter-placeholder="Filter Group"></ul>');
            listCreated = true;
            $("#group-list-container").trigger("create");
        }

        if(isCreator)
            $('<li data-icon="gear"><a href="'+groupID+'">'+groupName+'</a></li>').insertAfter("#group-created");
        else
            $('<li><a href="'+groupID+'">'+groupName+'</a></li>').insertAfter("#group-jointed");

        $("#grouplist").listview("refresh");

    }
*/