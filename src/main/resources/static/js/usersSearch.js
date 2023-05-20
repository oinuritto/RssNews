function searchUsers(query) {
    return fetch('/adminPanel/users?query=' + query)
        .then((response) => {
            return response.json()
        }).then((users) => {
            fillTable(users)
        })
}

function fillTable(users) {
    let tBody = document.getElementById("usersTbody");
    tBody.innerHTML = "";

    for (let i = 0; i < users.length; i++) {
        let row = tBody.insertRow(-1);
        let idCell = row.insertCell(0);
        let emailCell = row.insertCell(1);
        let firstNameCell = row.insertCell(2);
        let lastNameCell = row.insertCell(3);
        let roleEditCell = row.insertCell(4);
        let deleteCell = row.insertCell(5);

        idCell.innerHTML = users[i].id;
        emailCell.innerHTML = users[i].email;
        firstNameCell.innerHTML = users[i].firstName;
        lastNameCell.innerHTML = users[i].lastName;

        if (users[i].role === "ADMIN") {
            roleEditCell.innerHTML = "<form" +
                "                          action=\"/adminPanel/user/" + users[i].id + "/editRole\"\n" +
                "                          method=\"post\">\n" +
                "                            <input type=\"hidden\" name=\"role\" value=\"ADMIN\">" +
                "                            <button type=\"submit\" class=\"btn btn-primary btn-sm\" style=\"font-size: 0.8rem;\">\n" +
                "                                            Make USER\n" +
                "                            </button>\n" +
                "                     </form>";
        } else {
            roleEditCell.innerHTML = "<form" +
                "                          action=\"/adminPanel/user/" + users[i].id + "/editRole\"\n" +
                "                          method=\"post\">\n" +
                "                            <input type=\"hidden\" name=\"role\" value=\"USER\">" +
                "                            <button type=\"submit\" class=\"btn btn-primary btn-sm\" style=\"font-size: 0.8rem;\">\n" +
                "                                            Make ADMIN\n" +
                "                            </button>\n" +
                "                     </form>";
        }

        deleteCell.innerHTML = "<form action=\"/adminPanel/user/" + users[i].id + "/delete\"\n" +
            "                         method=\"post\">\n" +
            "                            <button type=\"submit\" class=\"btn btn-primary btn-sm\">Delete\n" +
            "                            </button>\n" +
            "                   </form>";
    }
}

document.getElementById("usersSearch").addEventListener("keyup", function () {
    searchUsers(document.getElementById('usersSearch').value);
})
