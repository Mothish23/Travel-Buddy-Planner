if (!isLoggedIn() || getRole() !== "ADMIN") {
    window.location.href = "dashboard.html";
}


async function loadAdminTrips() {
    try {
        const trips = await apiFetch("/admin/trips");
        const container = document.getElementById("adminTrips");
        container.innerHTML = "";
        trips.forEach(trip => {
            container.innerHTML += `
                <div class="mini-card">
                    <h3>
                        ${trip.tripName}
                    </h3>

                    <p>
                        📍 ${trip.destination}
                    </p>

                    <p>
                        Budget: ₹${trip.budget}
                    </p>

                    <button
                        class="btn danger small"
                        onclick="adminDeleteTrip(${trip.id})">
                        Delete Trip
                    </button>

                </div>
            `;
        });

    } catch (error) {
        alert(error.message);
    }
}


async function adminDeleteTrip(id) {

    if (!confirm("Delete this trip?")) {
        return;
    }

    await apiFetch(`/admin/trips/${id}`, {
        method: "DELETE"
    });

    loadAdminTrips();
}


loadAdminTrips();