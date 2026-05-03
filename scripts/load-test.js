import http from 'k6/http';
import { check, sleep } from 'k6';

// Test configuration
export const options = {
    stages: [
        { duration: '10s', target: 400 }, // Ramp up to 20 virtual users over 10 seconds
        { duration: '30s', target: 400 }, // Stay at 20 virtual users for 30 seconds
        { duration: '10s', target: 0 },  // Ramp down to 0 virtual users over 10 seconds
    ],
};

// Use your specific Minikube tunnel URL
const BASE_URL = 'http://127.0.0.1:57555/api/v1';

export default function () {
    // ----------------------------------------------------
    // 1. Test the POST /add endpoint
    // ----------------------------------------------------
    const payload = JSON.stringify({
        name: `LoadTestUser-${__VU}-${__ITER}`, // Unique name per virtual user and iteration
        age: Math.floor(Math.random() * 50) + 18, // Random age between 18 and 67
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    let postRes = http.post(`${BASE_URL}/add`, payload, params);

    // Verify the POST was successful
    check(postRes, {
        'POST /add returned 200': (r) => r.status === 200,
    });

    // ----------------------------------------------------
    // 2. Test the GET /users endpoint
    // ----------------------------------------------------
    let getRes = http.get(`${BASE_URL}/users`);

    // Verify the GET was successful
    check(getRes, {
        'GET /users returned 200': (r) => r.status === 200,
    });

    // Short pause to simulate real user think-time
    sleep(1);
}