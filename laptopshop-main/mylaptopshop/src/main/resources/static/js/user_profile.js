document.addEventListener("DOMContentLoaded", function () {
    // Define the API endpoint for getting user information
    const token = localStorage.getItem('authToken');
    const userId = localStorage.getItem('userId');
    const userApiEndpoint = `http://localhost:9193/api/v1/users/${userId}/user`;

    async function fetchUserData() {
        try {
            const response = await fetch(userApiEndpoint, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const userData = await response.json();
            updateUserProfile(userData);
        } catch (error) {
            console.error("Error fetching user data:", error);
        }
    }

    function updateUserProfile(user) {
        if (!user) return;
        const firstNameElement = document.getElementById("firstName");
        const lastNameElement = document.getElementById("lastName");
        const emailElement = document.getElementById("email");
        const passwordElement = document.getElementById("password");
        const userIdElement = document.getElementById("userId");

        if (userIdElement) userIdElement.textContent = user.id;
        if (firstNameElement) firstNameElement.textContent = user.firstName;
        if (lastNameElement) lastNameElement.textContent = user.lastName;
        if (emailElement) emailElement.textContent = user.email;
        if (passwordElement) passwordElement.textContent = "******" ;
    }

    fetchUserData();
});
