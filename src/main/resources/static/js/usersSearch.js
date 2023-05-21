let csrfHeader = updateCsrfHeader();
let csrfToken = updateCsrfToken();

function searchUsers(query) {
    fetch('/adminPanel/users?query=' + query, {
        method: "GET",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
            [csrfHeader]: csrfToken
        }
    })
        .then((response) => {
            return response.json()
        }).then((users) => {
        csrfHeader = updateCsrfHeader();
        csrfToken = updateCsrfToken();
        fillTable(users)
    })
}

function updateCsrfToken() {
    return document.querySelector('meta[name="_csrf"]').getAttribute('content');
}

function updateCsrfHeader() {
    return document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
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
        let roleCell = row.insertCell(4);
        let roleEditCell = row.insertCell(5);
        let deleteCell = row.insertCell(6);

        idCell.innerHTML = users[i].id;
        emailCell.innerHTML = users[i].email;
        firstNameCell.innerHTML = users[i].firstName;
        lastNameCell.innerHTML = users[i].lastName;
        roleCell.innerHTML = users[i].role;

        if (users[i].role === "ADMIN") {
            roleEditCell.innerHTML = "<form" +
                "                          action=\"/adminPanel/user/" + users[i].id + "/editRole\"\n" +
                "                          method=\"post\">\n" +
                "                            <input type=\"hidden\" name=\"role\" value=\"USER\">" +
                "                            <input type=\"hidden\" name=\"_csrf\" value=" + csrfToken + ">" +
                "                            <button type=\"submit\" class=\"btn btn-primary btn-sm\" style=\"font-size: 0.8rem;\">\n" +
                "                                            Make USER\n" +
                "                            </button>\n" +
                "                     </form>";
        } else {
            roleEditCell.innerHTML = "<form" +
                "                          action=\"/adminPanel/user/" + users[i].id + "/editRole\"\n" +
                "                          method=\"post\">\n" +
                "                            <input type=\"hidden\" name=\"role\" value=\"ADMIN\">" +
                "                            <input type=\"hidden\" name=\"_csrf\" value=" + csrfToken + ">" +
                "                            <button type=\"submit\" class=\"btn btn-primary btn-sm\" style=\"font-size: 0.8rem;\">\n" +
                "                                            Make ADMIN\n" +
                "                            </button>\n" +
                "                     </form>";
        }

        deleteCell.innerHTML = "<form action=\"/adminPanel/user/" + users[i].id + "/delete\"\n" +
            "                         method=\"post\">\n" +
            "                            <input type=\"hidden\" name=\"_csrf\" value=" + csrfToken + ">" +
            "                            <button type=\"submit\" class=\"btn btn-primary btn-sm\">Delete\n" +
            "                            </button>\n" +
            "                   </form>";
    }
}

document.getElementById("usersSearch").addEventListener("keyup", function () {
    searchUsers(document.getElementById('usersSearch').value);
})
