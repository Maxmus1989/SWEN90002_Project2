/****************************************************
* Student Name: Choong Teik Tan                     *
* Student Number: 568701                            *
* Student Email: choongt@student.unimelb.edu.au     *
* File: proj2-jscript.js (SWEN90002 Project 2)      *
****************************************************/

// Read a page's GET URL variables and return them as an associative array.
function getUrlVars()
{
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)
    {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}


$(document).on('pagebeforeshow', "#remove-member-dialog",function () {
    var checkbox = "";
    for(var i = 0; i < membersize; i++) {
        var id = memberlist[i].name.replace('@', ':');
        checkbox += '<label for="'+id+'">'+memberlist[i].name+'</label><input type="checkbox" id="'+id+'" class="custom" />';
    }

    $( "#to_clear" ).remove();
    var gid = groupID.replace("#group-info", '');
    var START = '<div data-role="fieldcontain" id="to_clear"><fieldset data-role="controlgroup" id="cb-group">';
    var END = '</fieldset></div>';
    $( "#cg" ).append( START+checkbox+END );
    $('input[type=checkbox]').checkboxradio().trigger('create');
});

$(document).on('pagebeforeshow', "#group-invitation",function () {
    $("#group-invitation-name").text( "Group Invitation" );
});

$(document).on('pagebeforeshow', "#group-info",function () {
    var gid = groupID.replace("#group-info", '');
    getGroupInfo($.cookie("email"), gid);
});

$(document).on('pagebeforeshow', "#groups",function () {
    getGroup($.cookie("email"));
});

$(document).on('pagebeforeshow', "#chatroom",function () {
    groupID = getUrlVars()["g"];
    
    // CHECK IF GROUP_ID EXIST && IS USER IS MEMBER OF THE GROUP
    //(IF NOT REDIRECT BACK)
    checkIsMember ($.cookie("email"), groupID);
    
});

$(document).on('pagebeforeshow', "#login",function () {
    //$("#group-invitation-name").text( "Group Invitation" );
    //window.location.href="memberpags.jsp#groups";
});

var groupNAME = "";
var membersize = 0;
var memberlist;
var groupID = getUrlVars()["g"];
var index = 0;

$(document).ready(function(){

    var groupid = "";

    $('textarea').autosize();

    window.history.forward();
    function noBack() {window.history.forward();}
    
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

    $("#login-button").click(function(){
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

    $("#reminder-email").keyup(function(event){
        if(event.keyCode == 13){
            $("#reminder-button").click();
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

    $("#register-email").keyup(function(event){
        if(event.keyCode == 13){
            $("#register-button").click();
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


    $("#quit-group-button").click(function(){
        $("#group-to-quit").text("[ "+$("#group-name").text()+" ]");
        window.location.href="#confirm-quit-group-dialog";
    });

    $("#confirm-quit-yes").click(function(){
        blocking_ui();
        quitGroup ($.cookie("email"), groupID);
    });

    $(".logout").click(function(){
        blocking_ui();
        logout();
    });

    $("#reset").click(function(){
        blocking_ui();  // blocking UI
        resetPassword();
    });

    $("#refresh-grouplist").click(function(){
        getGroup($.cookie("email"));
    });

    $("#confirm-remove-yes").click(function(){

        var list = new Array();
        var toremove = "";
        var isticked = false;
        var toremoveMember = ""
        
        for(var i=0; i < membersize; i++){
            var id2 = memberlist[i].name.replace('@', '\\:');
            var id = id2.replace(/\./g, '\\.');
            var box = "#"+id;

            if($(box).prop( "checked" )) {
                toremove+=memberlist[i].name+"<_>";
                isticked = true;
                list.push(memberlist[i].name);
                toremoveMember += memberlist[i].name+"<br>";
            }
        }

       if(isticked) {
            var type = "removeMember";
            var emailAdd = $.cookie("email");
            var groupid = groupID.replace('#group-info', '');;

            $.ajax({
                type: "POST",
                data: {type: type, email: emailAdd, groupid: groupid, toremove: toremove},//list},
                url: "GroupProcesses",
                success: function(data){
                    un_blocking_ui(); // unblocking UI
                    var status = data.status;

                    if(status === "success") {
                        //window.location.href="#group-info";
                        showGroupInfoMessage ("Success Removing Member", toremoveMember);
                    }
                    else if(status === "fail") {
                        showGroupInfoMessage ("ERROR Removing Member", "Please Try Again Later..");
                    }
                },
                error: function () {
                    un_blocking_ui(); // unblocking UI
                    showGroupInfoMessage ("ERROR Removing Member", "Please Try Again Later..");
                }
            });
       }
    });
    
    $("#confirm-delete-yes").click(function(){
        blocking_ui();
        deleteAccount();
    });


    $("#new-group-name").keyup(function(event){
        if(event.keyCode == 13){
            $("#confirm-create-group-yes").click();
        }
    });

    $("#confirm-create-group-yes").click(function(){
        var groupID = 'chatpage.jsp';
        var groupName = $("#new-group-name").val();
        var EmptyGroupNameCheck = groupName.replace(/^\s+|\s+$/, '');
        var emailAdd = $.cookie("email");

        if(EmptyGroupNameCheck != 0) {
            createGroup(emailAdd, groupName);
        }
        else {
            updateTips( "Group Name Must Not Be Empty!", ".validateTips-group", ".tips-to-display-group" );
        }
    });

    $("#message-contet").keyup(function(event){
        if(event.keyCode == 13){
            $("#send-message-button").click();
        }
    });


    // try to update content for every second.
    var intervalId = self.setInterval(function(){pull("")},1000);
    var donePull = true;

    $("#send-message-button").click(function(){
        user = $.cookie("email");
        var msg = $("#message-contet").val();
        var t = "Time";

        var EmptyMessageCheck = msg.replace(/^\s+|\s+$/, '');
        if(EmptyMessageCheck.length!=0) {
            // stop interval -> send message to server -> restart interval
            // prevent of blocking event (incase when concurrency - double protection as synchronized)
            self.clearInterval(intervalId);
            while(!donePull){}
            pull(msg);
            $("#message-contet").val('');
            intervalId = self.setInterval(function(){pull("")},1000);
        }
    });

    $("#invite-email").keyup(function(event){
        if(event.keyCode == 13){
            $("#invite-button").click();
        }
    });

    $("#invite-button").click(function(){
        var email_field = $("#invite-email");
        var email = email_field.val();
        var isValid = true;

        isValid = isValid && checkLength( email_field, "email", 6, 80, ".validateTips-invite", ".tips-to-display-invite" );
        isValid = isValid && checkRegexp( email_field, /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i, "Invalid Email Format!\n E.G: [xxx@student.unimelb.edu.au]", ".validateTips-register", ".tips-to-display-register" );

        if(isValid) {
            blocking_ui();
            addMember ($.cookie("email"), groupID, email);
        }

    });

    // send message to "Chat" servlet and wait for reply of all new message that not yet display.
    // sending empty message to "Chat" servlet mean just to request if there any new message updated.
    // returned json :: { "msghead":"2", "newmessage":[{"user":"user-1", "message":"message-1", "timestamp":"time-1"},{"user":"user-2", "message":"message-2", "timestamp":"time-2"}] }
    // "msghead" = total number of new message received from servlet.
    // "newmessage" = a list of all new message (if msghead == 2 which mean there will have 2 new messages element in "newmessage")
    //var index = 0;
    var user = "Me";
    
    function pull(msg) {
        if(donePull) {
            donePull = false;
            $.getJSON("Chat", {
                "type" : "pull",
                "user": user,
                "message": msg,
                "msghead" : index,
                "groupid" : groupID},
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

    function displayMessage(usr, msg, t) {
        $( "<div class=\"bubble\"><p>" + msg + "</p></div>" +
           "<p class=\"center\">" + t + " -- " + usr +"</p>"
        ).appendTo( "#message-display" );
        index++;
        toBottom();
    }

    $(".groups-button").click(function(){
        blocking_ui();  // blocking UI
        //getGroup($.cookie("email"));
        window.location.href="#groups";
    });

    $("#confirm-delete-group-yes").click(function(){
        var gid = groupID.replace("#group-info", '');
        deleteGroup ($.cookie("email"), gid);
    });

});

    function deleteGroup (emailAdd, groupID) {
        var type = "deleteGroup";
        $.ajax({
            type: "POST",
            data: {type: type, email: emailAdd, groupid: groupID},
            url: "GroupProcesses",
            success: function(data){
                un_blocking_ui(); // unblocking UI
                var status = data.status;

                if(status === "success") {
                    window.location.href="memberpage.jsp#groups";
                }
                else if(status === "fail") {
                    showGroupInfoMessage ("ERROR Deleting Group", "Please Try Again Later..");
                }
            },
            error: function () {
                un_blocking_ui(); // unblocking UI
                showGroupInfoMessage ("ERROR Deleting Group", "Please Try Again Later..");
            }
        });
    }

    function quitGroup (emailAdd, groupID) {
        var type = "quitGroup";
        $.ajax({
            type: "POST",
            data: {type: type, email: emailAdd, groupid: groupID},
            url: "GroupProcesses",
            success: function(data){
                un_blocking_ui(); // unblocking UI
                var status = data.status;

                if(status === "success") {
                    window.location.href="memberpage.jsp#groups";
                }
                else if(status === "fail") {
                    showGroupInfoMessage ("ERROR Quiting Group", "Please Try Again Later..");
                }
            },
            error: function () {
                un_blocking_ui(); // unblocking UI
                showGroupInfoMessage ("ERROR Quiting Group", "Please Try Again Later..");
            }
        });
    }

    function getGroupInfo(emailAdd, groupID) {
        var title = 'Unknow Error Occur';
        var message = 'Please Try Again Later..<br>If it happen again please Report to choongt@student.unimelb.edu.au';

        // Request TYPE
        var type = "getGroupInfo";

        $.ajax({
            type: "POST",
            data: {type: type, email: emailAdd, groupid: groupID},
            url: "GroupProcesses",
            success: function(data){
                $( ".info-data" ).remove();
                $( ".info-data-member" ).remove();
                
                var isowner = data.isowner;

                var owned = data.owned;

                var groupname = data.groupname;
                $("#group-name").text( groupname );
                groupNAME = groupname;

                $('<li data-icon="gear" class="info-data"><a href="#">'+owned+'</a></li>').insertAfter("#group-owner");

                var totalMember = data.totalmember;
                var member = data.member;
                membersize = totalMember;
                memberlist = member;

                for (var i=0; i<totalMember; i++) {
                    $('<li  class="info-data-member"><a href="#">'+member[i].name+'</a></li>').insertAfter("#group-member");
                }
                
                $("#group-member-list").listview("refresh");

                if(isowner) {
                    $( "#quit-group-button" ).hide();
                }
                else {
                    $( "#invite-user-button" ).hide();
                    $( "#delete-user-button" ).hide();
                    $( "#delete-group-button" ).hide();
                }


                un_blocking_ui();  // unblocking UI
            },
            error: function () {
                un_blocking_ui();  // unblocking UI
                showGroupInfoMessage ("ERROR Loading Group Information", "Please Try Again Later..");
            }
        });
    }


    function addMember (emailAdd, groupID, newmember) {
        var type = "invite";
        var gid = groupID.replace("#group-info", '');
        $.ajax({
            type: "POST",
            data: {type: type, email: emailAdd, groupid: gid, newmember: newmember},
            url: "GroupProcesses",
            success: function(data){
                un_blocking_ui(); // unblocking UI
                var status = data.status;

                if(status === "isMember") {
                    showGroupInfoMessage ("ERROR ADDING MEMBER", "Server Detected User Already A Member Of Current Group!");
                }
                else if(status === "isOwner") {
                    showGroupInfoMessage ("ERROR ADDING MEMBER", "You Cannot Add Your Self To Join Group Owned By You!");
                }
                else if(status === "success") {
                    showGroupInfoMessage ("Invitation Sent", "An Invitation Email Had Just Been Sent to<br>"+newmember);
                }
                else if(status === "sendMailFail") {
                    showGroupInfoMessage ("Error Sending Invitation Email", "Please Try Again Later<br>"+newmember);
                }
                //window.location.href="#group-info";
            },
            error: function () {
                un_blocking_ui(); // unblocking UI
                showGroupInfoMessage ("ERROR ADDING MEMBER", "Please Try Again Later..");
            }
        });
    }
    
    function checkIsMember (emailAdd, groupID) {
        var type = "ismember";
        $.ajax({
            type: "POST",
            data: {type: type, email: emailAdd, groupid: groupID},
            url: "GroupProcesses",
            success: function(data){
                un_blocking_ui(); // unblocking UI
                var status = data.status;

                if(status === "false") {
                    window.location.href="memberpage.jsp";
                }
            },
            error: function () {
                un_blocking_ui(); // unblocking UI
                window.location.href="memberpage.jsp";
            }
        });
    }

    function getGroup(emailAdd) {
        var title = 'Unknow Error Occur';
        var message = 'Please Try Again Later..<br>If it happen again please Report to choongt@student.unimelb.edu.au';

        // Request TYPE
        var type = "getlist";

        $.ajax({
            type: "POST",
            data: {type: type, email: emailAdd},
            url: "GroupProcesses",
            success: function(data){
                $( ".group-data" ).remove();
                var groupID = 'chatpage.jsp';

                var totalOwned = data.totalowned;
                if(totalOwned > 0) {
                    var owned = data.owned;

                    for (var i=0; i<totalOwned; i++) {
                        $('<li class="group-data" data-icon="gear"><a href="'+groupID+'?g='+owned[i].id+'" rel="external">'+owned[i].name+'</a></li>').insertAfter("#group-created");
                    }
                }

                var totalJoined = data.totaljoined;
                if(totalJoined > 0) {
                    var joined = data.joined;

                    for (var i=0; i<totalJoined; i++) {
                        $('<li  class="group-data"><a href="'+groupID+'?g='+joined[i].id+'" rel="external">'+joined[i].name+'</a></li>').insertAfter("#group-jointed");
                    }
                }
                un_blocking_ui();  // unblocking UI
                $("#grouplist").listview("refresh");
                
            },
            error: function () {
                un_blocking_ui();  // unblocking UI
                showMemberMessage(title, message);
            }
        });
    }


    function createGroup(emailAdd, groupName) {
        var title = 'Unknow Error Occur';
        var message = 'Please Try Again Later..<br>If it happen again please Report to choongt@student.unimelb.edu.au';

        // Request TYPE
        var type = "create";
        
        $.ajax({
            type: "POST",
            data: {type: type, email: emailAdd, groupname: groupName},
            url: "GroupProcesses",
            success: function(data){
                var status = data.status;
                var groupname = data.groupname;
                var isowner = data.isowner;

                un_blocking_ui();  // unblocking UI
                // invalidEmail || exist
                if (status === "success") {
                   $("#new-group-name").val('');
                   showMemberMessage("New Group Created", "New Group ("+groupname+") Successfully Created! :)");
                }
                else {

                    if (status === "exist") {
                        title = 'Same Group Name';
                        message = '<h2>( '+groupname+' )</h2><br>Server Detedted Group Name Had Been Created By You For Other Group!';
                    }

                    showMemberMessage(title, message);
                }
            },
            error: function () {
                un_blocking_ui();  // unblocking UI
                showMemberMessage(title, message);
            }
        });
    }


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

    function showGroupInfoMessage (title, data) { // display message
        $("#inform-message-content").text("");
        $("#inform-message-title").text("");
        var d = data.split("<br>");
        $("#inform-message-title").text(title);
        for ( var i = 0, l = d.length; i < l; i++ )
            $("#inform-message-content").append( "<center><p>"+d[i]+"</p></center>" );
        $.mobile.changePage( "#chatpage-message-dialog" );//, { transition: "slideup"} );
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

    function resetPassword() {
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
    }

    function deleteAccount() {
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
    }

    // cookie expire
    function getExpiresMinutes (minutes) {
        var date = new Date();
        //var minutes = 30; // 30 minutes
        date.setTime(date.getTime() + (minutes * 60 * 1000));
        return date;
    }

    function toBottom() {
        $(document).scrollTop($(document).height());    // Auto Scroll to Bottom
    }
