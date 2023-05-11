const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

function setLike(articleId) {
    fetch('/api/likes/add/' + articleId, {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
            [csrfHeader]: csrfToken
        }
    })
        .then((response) => {
            if (response.ok) {
                document.getElementById("ifLiked-article-" + articleId).setAttribute("value", "liked");
                document.getElementById("likedCount-article-" + articleId).className = "bi bi-hand-thumbs-up-fill";
                return update(articleId);
            }
        }).catch(error => {
        console.error('Ошибка при отправке запроса', error);
    });
}

function dislike(articleId) {
    fetch('/api/likes/article/' + articleId, {
        credentials: "include",
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            [csrfHeader]: csrfToken
        }
    })
        .then((response) => {
            if (response.ok) {
                document.getElementById("ifLiked-article-" + articleId).setAttribute("value", "nonLiked");
                document.getElementById("likedCount-article-" + articleId).className = "bi bi-hand-thumbs-up";
                return update(articleId);
            }
        }).catch(error => {
        console.error('Ошибка при отправке запроса', error);
    });
}

function workWithLike(articleId) {
    if (document.getElementById("ifLiked-article-" + articleId).getAttribute("value") === "liked") {
        dislike(articleId);
    } else {
        setLike(articleId);
    }
}

function update(articleId) {
    return fetch('/api/likes/article/' + articleId + '/count')
        .then((response) => {
            return response.json();
        }).then((likes) => {
            let likedCount = document.getElementById("likedCount-article-" + articleId);
            likedCount.innerHTML = likes;
        })
}

function updateIcons(userId) {
    fetch('/api/likes/user/' + userId)
        .then((response) => {
            return response.json()
        })
        .then((likes) => {
            for (let i = 0; i < likes.length; i++) {
                let forFill = document.getElementById("likedCount-article-" + likes[i].articleId);
                let forValue = document.getElementById("ifLiked-article-" + likes[i].articleId);

                if (forFill != null && forValue != null) {
                    forFill.className = "bi bi-hand-thumbs-up-fill";
                    forValue.setAttribute("value", "liked");
                }
            }
        })
        .catch(error => {
            console.error('Ошибка при отправке запроса', error);
        });
}


const userIdElement = document.querySelector(".userId");
const likeButtons = document.querySelectorAll('.like-button');

// устанавливаем обработчик клика на каждую кнопку
likeButtons.forEach(button => {
    button.addEventListener('click', () => {
        let articleId = button.id.split("-").at(2);
        workWithLike(articleId);
    });
});

if (userIdElement != null) {
    document.addEventListener("DOMContentLoaded", function () {
        updateIcons(userIdElement.id);
    });
}
