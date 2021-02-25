var ws;
var currentGroup = '';
var groupHashKey;
var filterList;

var username = localStorage.getItem("username");

function connect() {

    var host = document.location.host;
    var pathname = document.location.pathname;

    ws = new WebSocket("ws://" + host + pathname + "/chat/" + username + "/" + groupHashKey[currentGroup]);
    console.log(ws);
    console.log(host);
    console.log(pathname);

    ws.onmessage = function (event) {
        console.log(event.data);
        var message = JSON.parse(event.data);
        createMessage(message);
    };
}

function send() {

    var content = document.getElementById("message_value").value;
    var json = JSON.stringify({
        "content": content
    });
    ws.send(json);
}

function fillGroupList() {
    groupHashKey = {};
    const xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", 'http://localhost:8080/prattle/rest/user/groups/' + username, false);
    xmlHttp.send(null);
    let groupList = JSON.parse(xmlHttp.response);

    for (let i = 0; i < groupList.length; i++) {
        var groupId = groupList[i];
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "http://localhost:8080/prattle/rest/group/group/" + groupId, false);
        xhr.send(null);
        var groupTemp = JSON.parse(xhr.response);
        var newChannel = document.createElement('li');
        var groupName = groupTemp.groupName;
        groupHashKey[groupName] = '' + groupId;
        if (groupTemp.type === false) {
            loadPublicChannel(newChannel, groupName);
        } else {
            loadPrivateChannel(newChannel, groupName);
        }
    }
}

function switchChannel(selectedChannel) {
    if (currentGroup === selectedChannel.id) {
        document.getElementById("message_history").innerHTML = "";
        fillOldMessages();
    } else {
        document.getElementById("message_history").innerHTML = "";
        currentGroup = selectedChannel.id;
        var actives = document.getElementsByClassName("channel active");
        for (var i = 0; i < actives.length; i++) {
            actives[i].className = "channel";
        }
        document.getElementById(selectedChannel.id).className = "channel active";
        currentGroup = selectedChannel.id;
        if(isModerator(groupHashKey[currentGroup])){
            moderatorGroupControl(selectedChannel);
        }else{
            regularGroupControl(selectedChannel)
        }
        connect();
        fillOldMessages();
    }
}

function createMessage(message) {
    var newMsg = document.createElement('div');
    var messageAuthor = message.from;
    var date = message.recordCreateDate;
    var day = date.substring(0, 10);
    var time = date.substring(12,16);
    date = day + " " + time;
    newMsg.innerHTML = "<div class='message'><a class='message_profile-pic'" +
        " href=" + loadProfilePic(messageAuthor) + ">" +
        "<img src=" + loadProfilePic(messageAuthor) + " alt=\"Avatar\" class=\"message_profile-pic\"></a>" +
        "<a class='message_username'>" + message.from + "</a>" +
        "<span class='message_timestamp'>" + date + "</span>" +
        "<span class='message_star'></span>" +
        "<span class='message_content'>" + checkFilter(message.content) + "</span></div>";
    document.getElementById("message_history").appendChild(newMsg);
}

function userMenu() {
    var checkUser = username;
    if (username.length > 14) {
        checkUser = username.substring(0, 12) + "..";
    }
    var user = document.createElement('div');
    user.innerHTML = "<div id='UserName' class='dropdown'>" + "<a class='user-menu_profile-pic'" +
        " href=" + loadProfilePic(username) + "><img src=" + loadProfilePic(username) +
        " alt=\"Avatar\" class=\"user-menu_profile-pic\"></a><a class='user-menu_profile-pic'></a>" + checkUser +
        "<div class='dropdown-content'>" +
        "<a href='#' onclick='setStatus(\"online\")'>Online</a>" +
        "<a href='#' onclick='setStatus(\"away\")'>Away</a>" +
        "<a href='#' onclick='setStatus(\"busy\")'>Busy</a>" +
        "<a href='#' onclick='setStatus(\"offline\")'>Appear Offline</a>" +
        "<a href='#' onclick='openSettings()'>Settings</a>" +
        "<a href='#' onclick='setStatus(\"offline\");logout()'>Logout</a>" +
        "</div><span id=\"status\" class='online'></span></div>";

    document.getElementById("userMenu").appendChild(user);
}

function setStatus(statusChange) {
    switch (statusChange) {
        case "online":
            document.getElementById("status").className = "online";
            break;
        case "away":
            document.getElementById("status").className = "away";
            break;
        case "busy":
            document.getElementById("status").className = "busy";
            break;
        case "offline":
            document.getElementById("status").className = "offline";
            break;
        default:
            document.getElementById("status").className = "online";
            break;
    }
    changeStatus();
}

function loadPublicChannel(newChannel, groupName) {
    newChannel.innerHTML = "<li onmouseover='' style='cursor: pointer' onclick='switchChannel(this)' " +
        "class='channel'" + " id='" + groupName + "'><a class='channel_name'>" +
        "<span><span class='prefix'>#</span>" + groupName + "</span></a></li>";
    document.getElementById("channelList").appendChild(newChannel);
}

function loadPrivateChannel(newChannel, groupName) {
    var listMembers = getGroupMembers1(groupName);
    var tempUsername;

    for(let i = 0; i < listMembers.length; i++){
        var tempMember = listMembers[i];
        if(tempMember !== username){
            tempUsername = tempMember;
        }
    }

    newChannel.innerHTML = "<li onmouseover='' style='cursor: pointer' onclick='switchChannel(this)' " +
        "class='channel'" + " id='" + groupName + "'><a class='channel_name'>" +
        "</span>" + tempUsername + "</span></a></li>";
    document.getElementById("channelListDirect").appendChild(newChannel);
}
function getGroupMembers1(groupName) {
    const groupMembersHttp = new XMLHttpRequest();
    groupMembersHttp.open("GET", 'http://localhost:8080/prattle/rest/group/members/' + groupHashKey[groupName], false);
    groupMembersHttp.send(null);
    return JSON.parse(groupMembersHttp.response);
}

function loadProfilePic(author) {
    const avatarHttp = new XMLHttpRequest();
    avatarHttp.open("GET", 'http://localhost:8080/prattle/rest/user/avatar/' + author, false);
    avatarHttp.send(null);
    return JSON.parse(avatarHttp.response);
}

function moderatorGroupControl(selectedChannel) {
    document.getElementById("currChannel").innerHTML = "<span class='channel-menu_name'>" +
        "<span class='channel-menu_prefix'>#</span>" + selectedChannel.id + "<span class='dropdown-content'>" +
        "<a href='#' onclick='openGroupUsers()'>UserOptions</a>" +
        "<a onclick='deleteGroup()' href='#'>Disband Group</a></span></span>";
}

function regularGroupControl(selectedChannel) {
    document.getElementById("currChannel").innerHTML = "<span class='channel-menu_name'>" +
        "<span class='channel-menu_prefix'>#</span>" + selectedChannel.id + "<span class='dropdown-content'>" +
        "<a href='#' onclick='leaveGroup()'>Leave Group</a></span></span>";
}

function changeStatus() {

    var tempStatus = document.getElementById("status").className;
    const statusHttp = new XMLHttpRequest();
    statusHttp.open('POST', 'http://localhost:8080/prattle/rest/user/status/' + username + "/" + tempStatus, false);
    statusHttp.send(null);
    statusHttp.onload = function () {
        alert("Status Change Successful")
    }
}

function register() {
    var getUsername = document.getElementById("username").value;
    var getPassword = document.getElementById("password").value;
    const http = new XMLHttpRequest();
    http.open('POST', 'http://localhost:8080/prattle/rest/user/create/' + getUsername + "/" + getPassword);
    http.send(null);
    http.onload = function () {
        alert("Registration Successful!")
    }
}

function fillOldMessages() {
    const oldMessageHttp = new XMLHttpRequest();
    oldMessageHttp.open("GET", 'http://localhost:8080/prattle/rest/message/group/' + groupHashKey[currentGroup], false);
    oldMessageHttp.send(null);

    let messageList = JSON.parse(oldMessageHttp.response);
    for (let i = 0; i < messageList.length; i++) {
        var oldMessage = messageList[i];
        createMessage(oldMessage);
    }
}

function openGroupCreation() {
    var groupCreation = document.querySelector('#groupCreation')
    groupCreation.style.display = 'block';
}

function closeGroupCreation() {

    var groupCreation = document.querySelector('#groupCreation')
    groupCreation.style.display = 'none';
}

function openDirectCreation() {
    var directCreation = document.querySelector('#directCreation')
    directCreation.style.display = 'block';
}

function closeDirectCreation() {

    var directCreation = document.querySelector('#directCreation')
    directCreation.style.display = 'none';
}

function openChangePassword() {
    var changePass = document.querySelector('#changePassCreation')
    changePass.style.display = 'block';
}

function closeChangePassword() {
    var changePass = document.querySelector('#changePassCreation')
    changePass.style.display = 'none';
    document.getElementById("oldPass").value = "";
    document.getElementById("newPass").value = "";

}


function openGroupUsers() {
    document.getElementById("createGroupUsersList").innerHTML = "";
    var groupUsers = document.querySelector('#groupUsersCreation')
    groupUsers.style.display = 'block';
    var tempMember;
    let groupMemberList = getGroupMembers();
    for(let i = 0; i < groupMemberList.length; i++) {
        tempMember = groupMemberList[i];
        var createdGroupUsers = document.createElement('div');
        createdGroupUsers.innerHTML = "<li <div id="+tempMember + "ID" +" class='groupDropdown'>" + "<a class='group-menu_profile-pic'" +
            " href=" + loadProfilePic(tempMember) + "><img src=" + loadProfilePic(tempMember) +
            " alt=\"Avatar\" class=\"group-menu_profile-pic\"></a><a class='group-menu_profile-pic'></a>" + tempMember +
            "<div class='groupDropdown-content'>" +
            "<a onclick='removeGroupMembers(\"" + tempMember + "\");'>Remove</a>" +
            "<a onclick='promoteGroupMembers(\"" + tempMember + "\");'>Promote</a></li>";
        document.getElementById("createGroupUsersList").appendChild(createdGroupUsers);
    }
}
function getGroupMembers() {
    const groupMembersHttp = new XMLHttpRequest();
    groupMembersHttp.open("GET", 'http://localhost:8080/prattle/rest/group/members/' + groupHashKey[currentGroup], false);
    groupMembersHttp.send(null);
    return JSON.parse(groupMembersHttp.response);
}

function addGroupMembers() {
    if (event.keyCode === 13 || event.which === 13) {  //checks whether the pressed key is "Enter"
        var user = document.getElementById("searchBarCreateGroupUsers").value;
        if (userExist(user)) {
            if (user === localStorage.getItem("username")) {
                alert("Member already apart of group");
            } else {
                const addGroupMembersHttp = new XMLHttpRequest();
                addGroupMembersHttp.open("POST", 'http://localhost:8080/prattle/rest/group/add/' + groupHashKey[currentGroup] + '/' + user, false);
                addGroupMembersHttp.send(null);
                alert(user + "has been added")
            }
        } else {
            alert("UserName does not exist");
        }
        document.getElementById("searchBarCreateGroupUsers").value = "";
        openGroupUsers();
    }
}

function removeGroupMembers(value) {

    const removeGroupMembersHttp = new XMLHttpRequest();
    removeGroupMembersHttp.open("POST", 'http://localhost:8080/prattle/rest/group/remove/' + groupHashKey[currentGroup] + '/' + value, false);
    removeGroupMembersHttp.send(null);
    alert(value + "has been removed")
    openGroupUsers();
}

function promoteGroupMembers(value) {
    const promoteGroupMembersHttp = new XMLHttpRequest();
    promoteGroupMembersHttp.open("POST", 'http://localhost:8080/prattle/rest/group/promote/' + groupHashKey[currentGroup] + '/' + value, false);
    promoteGroupMembersHttp.send(null);
    alert(value + "has been promoted")
    openGroupUsers();
}

function closeGroupUsers() {

    var directCreation = document.querySelector('#groupUsersCreation')
    directCreation.style.display = 'none';
}

function sendMsg() {
    if (event.keyCode === 13 || event.which === 13) {  //checks whether the pressed key is "Enter"
        var msg = document.getElementById("message_value").value;
        if(msg === null || msg.trim() === "" ){
            document.getElementById("message_value").value = "";
        }else{
            send();
            document.getElementById("message_value").value = "";
        }
    }
}

function submitGroupCreation() {
    if (event.keyCode === 13 || event.which === 13) {  //checks whether the pressed key is "Enter"
        var groupMemberName = document.getElementById("searchBarCreateGrp").value;
        var createdGroups = document.createElement('div');
        if (userExist(groupMemberName)) {
            if (groupMemberName === localStorage.getItem("username")) {
                alert("You will automatically be added to the group");
            } else {
                createdGroups.innerHTML = "<li class='list_group_member_names'>" + groupMemberName + "</li>"
                document.getElementById("createGroupNameList").appendChild(createdGroups);
            }
        } else {
            alert("UserName does not exist");
        }
        document.getElementById("searchBarCreateGrp").value = "";
    }
}

function submitDirectCreation() {
    if (event.keyCode === 13 || event.which === 13) {  //checks whether the pressed key is "Enter"
        var directMemberName = document.getElementById("searchBarCreateDirect").value;
        var createdGroups = document.createElement('div');
        if (document.getElementById("createDirectNameList").children.length < 1) {
            if (userExist(directMemberName)) {
                if (directMemberName === localStorage.getItem("username")) {
                    alert("You will automatically be added to the group");
                } else {
                    createdGroups.innerHTML = "<li class='list_direct_member_names'>" + directMemberName + "</li>"
                    document.getElementById("createDirectNameList").appendChild(createdGroups);
                }
            } else {
                alert("UserName does not exist");
            }
        }
        else {
            alert("Cannot Add Multiple Members")
        }
        document.getElementById("searchBarCreateDirect").value = "";
    }
}


function openSettings() {
    var settingsPane = document.querySelector("#settings-pane");
    settingsPane.style.display = 'block';
}

function closeSettings() {
    var settingsPane = document.querySelector("#settings-pane");
    settingsPane.style.display = 'none';
}

function showDropdownChoices(selectElement) {
    document.getElementById(selectElement).classList.toggle("show")
}

function filterOption(selection) {
    document.getElementById("filterBtn").textContent = "Mature Filter: " + selection;
}

function groupMessageCreation() {

    var groupMemberList = [];
    groupMemberList.push(username);
    var groupMembers = document.getElementsByClassName("list_group_member_names");
    for (let i = 0; i < groupMembers.length; i++) {
        let tempMember = groupMembers.item(i).innerHTML;
        groupMemberList.push(tempMember);
    }
    var groupModeratorList = [];
    groupModeratorList.push(username);
    var groupName = document.getElementById("createGroupName").value;
    if(groupName !== "") {
        const groupCreationParams = {
            "groupName": groupName,
            "groupList": groupMemberList,
            "type": false,
            "moderatorList": groupModeratorList
        };
        console.log(groupCreationParams);

        const groupCreationHttp = new XMLHttpRequest();
        groupCreationHttp.open('POST', 'http://localhost:8080/prattle/rest/group/create');
        groupCreationHttp.setRequestHeader('Content-type', 'application/json');
        groupCreationHttp.send(JSON.stringify(groupCreationParams));
        groupCreationHttp.onload = function () {
            window.location.href = 'chatRoom.html';
        }
    }
    if(groupName === ""){
        alert("Please add group name");
    }

}

function login() {
    var passwordLogin = document.getElementById("password").value;
    var usernameLogin = document.getElementById("username").value;
    const loginHttp = new XMLHttpRequest();
    loginHttp.open('GET', 'http://localhost:8080/prattle/rest/user/login/' + usernameLogin + "/" + passwordLogin, false);
    loginHttp.send(null);
    console.log(loginHttp.response);
    if (loginHttp.response === "Success") {
        localStorage.setItem("username", document.getElementById('username').value);
        window.location.href = 'chatRoom.html';
    } else {
        document.getElementById("password").value = "";
        document.getElementById("username").value = "";
        alert("Incorrect Username or Password");
    }
}

function directMessageCreation() {
    var directMemberList = [];
    var groupModeratorList = [];
    groupModeratorList.push(username);
    try {
        var directMember = document.getElementsByClassName("list_direct_member_names").item(0).innerHTML;

        directMemberList.push(directMember);
        directMemberList.push(username);
        const directGroupParams = {
            "groupName": directMember,
            "groupList": directMemberList,
            "type": true,
            "moderatorList": groupModeratorList
        };

        console.log(directGroupParams);
        const directHttp = new XMLHttpRequest();
        directHttp.open('POST', 'http://localhost:8080/prattle/rest/group/create');
        directHttp.setRequestHeader('Content-type', 'application/json');
        directHttp.send(JSON.stringify(directGroupParams));
        directHttp.onload = function () {
            window.location.href = 'chatRoom.html';
        }
    }catch (e) {
        alert("Please add direct message user")
    }
}


function userExist(groupMemberName) {
    const existHttp = new XMLHttpRequest();
    existHttp.open('GET', 'http://localhost:8080/prattle/rest/user/exist/' + groupMemberName, false);
    existHttp.send(null)
    var response = existHttp.response;
    return response === "True";
}

function addGroupClick() {
    alert("Clicked");
}

function filter() {
    const filterHttp = new XMLHttpRequest();
    filterHttp.open('GET', 'http://localhost:8080/prattle/rest/filter/filter', false);
    filterHttp.send(null);
    filterList = filterHttp.response;
}

function checkFilter(msg) {
    if (document.getElementById("filterBtn").textContent === "Mature Filter: On") {
        var filteredMsg = "";
        var words = msg.split(" ");
        for (var i = 0; i < words.length; i++) {
            if (filterList.indexOf(words[i].toLowerCase()) !== -1) {
                filteredMsg += "**** ";
            } else {
                filteredMsg += words[i] + " ";
            }
        }
        return translate(filteredMsg.substring(0, filteredMsg.length - 1));
    }
    return translate(msg);
}

function setNoTranslation() {
    document.getElementById("translationBtn").textContent = "Translation: None";
}

function setLanguage(lang) {
    document.getElementById("translationBtn").textContent = "Language: " + lang;
}

function translate(msg) {
    if (getLang() === "None") {
        return msg;
    }else{
        const translateHttp = new XMLHttpRequest();
        translateHttp.open('GET', 'http://localhost:8080/prattle/rest/translation/translation/' + getLang() + '/' + msg, false);
        translateHttp.send(null);
        var translated = translateHttp.response;
        return translated.substring(1, translated.length - 1);
    }
}

function getLang() {
    var lang = document.getElementById("translationBtn").textContent;
    switch (lang) {
        case "Translation: None":
            return 'None'
        case "Language: Czech":
            return 'cs';
        case "Language: Danish":
            return 'da';
        case "Language: Dutch":
            return 'nl';
        case "Language: English":
            return 'en';
        case "Language: Finnish":
            return 'fi';
        case "Language: French":
            return 'fr';
        case "Language: German":
            return 'de';
        case "Language: Hindi":
            return 'hi';
        case "Language: Italian":
            return 'it';
        case "Language: Polish":
            return 'pl';
        case "Language: Spanish":
            return 'es';
        case "Language: Tagalog":
            return 'tl';
    }

}

function deleteGroup() {
    const statusHttp = new XMLHttpRequest();
    statusHttp.open('POST', 'http://localhost:8080/prattle/rest/group/delete/' + groupHashKey[currentGroup], false);
    statusHttp.send(null);
    alert("Delete Successful");
    window.location.href ="chatRoom.html"
}

function logout() {
    window.localStorage.clear();
    window.location.href = 'index.html'
}

function isModerator(groupID) {
    const modHTTP = new XMLHttpRequest();
    modHTTP.open('GET', 'http://localhost:8080/prattle/rest/group/isModerator/' + groupID + "/" + username, false);
    modHTTP.send(null);
    var modList = modHTTP.response;
    if(modList === "true"){
        return true;
    }
    else {
        return false;
    }
}

function confirmChangePass(){
    var oldPass = document.getElementById("oldPass").value;
    var newPass = document.getElementById("newPass").value;

    const changePassHTTP = new XMLHttpRequest();
    changePassHTTP.open('GET', 'http://localhost:8080/prattle/rest/user/changePassword/' + username +
        "/" + oldPass + "/" + newPass, false);
    changePassHTTP.send(null);
    var response = changePassHTTP.response;
    if(response === "True"){
        closeChangePassword();
        alert("Password Successfully Changed");
    }else{
        alert("Your password is incorrect")
    }
    document.getElementById("oldPass").value = "";
    document.getElementById("newPass").value = "";

}

function leaveGroup() {
    const leaveGroupHTTP = new XMLHttpRequest();
    leaveGroupHTTP.open('POST', 'http://localhost:8080/prattle/rest/group/leave/' + groupHashKey[currentGroup] + "/" + username, false);
    leaveGroupHTTP.send(null);
    alert("You have successfully left " + currentGroup);
    window.location.href = 'chatRoom.html';
}
