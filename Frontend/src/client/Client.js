import axios from 'axios';

const API_BASE = process.env.REACT_APP_API_URL || "http://localhost:8080";
const API_VERSION = "/api/v1";

// Generic GET
const fetchGetData = (uri) => {
    const url = `${API_BASE}${API_VERSION}${uri}`;
    return axios.get(url)
        .catch((error) => {
            console.error('Error fetching data from url:', url);
            throw error;
        });
};

// Generic POST without auth
const fetchPostData = (uri, payload) => {
    const url = `${API_BASE}${API_VERSION}${uri}`;
    return axios.post(url, payload)
        .catch((error) => {
            console.error('Error fetching data from url:', url);
            throw error;
        });
};

// POST with auth (token)
const fetchPostDataWithAuth = (uri, payload) => {
    const token = localStorage.getItem('token');
    const url = `${API_BASE}${API_VERSION}${uri}`;
    return axios.post(url, payload, {
        headers: {
            "accept": "application/json",
            "content-type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    }).catch((error) => {
        console.error('Error fetching data from url:', url, error.message);
        throw error;
    });
};

// PUT with auth
const fetchPutDataWithAuth = (uri, payload) => {
    const token = localStorage.getItem('token');
    const url = `${API_BASE}${API_VERSION}${uri}`;
    return axios.put(url, payload, {
        headers: {
            "accept": "application/json",
            "content-type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    }).catch((error) => {
        console.error('Error fetching data from url:', url, error.message);
        throw error;
    });
};

// File upload with auth
const fetchPostFileUploadWithAuth = async (uri, formData) => {
    const token = localStorage.getItem('token');
    const url = `${API_BASE}${API_VERSION}${uri}`;
    try {
        const response = await axios.post(url, formData, {
            headers: {
                "accept": "*/*",
                "Authorization": `Bearer ${token}`,
                "Content-Type": "multipart/form-data"
            }
        });
        return response;
    } catch (error) {
        console.error('Error fetching data from url:', url, error.message);
        throw error;
    }
};

// GET with auth
const fetchGetDataWithAuth = async (uri) => {
    const token = localStorage.getItem('token');
    const url = `${API_BASE}${API_VERSION}${uri}`;
    try {
        const response = await axios.get(url, {
            headers: { "Authorization": `Bearer ${token}` }
        });
        return response;
    } catch (error) {
        console.error('Error fetching data from url:', url, error.message);
        throw error;
    }
};

// DELETE with auth
const fetchDeleteDataWithAuth = async (uri) => {
    const token = localStorage.getItem('token');
    const url = `${API_BASE}${API_VERSION}${uri}`;
    try {
        const response = await axios.delete(url, {
            headers: { "Authorization": `Bearer ${token}` }
        });
        return response;
    } catch (error) {
        console.error('Error fetching data from url:', url, error.message);
        throw error;
    }
};

// GET ArrayBuffer (binary) with auth
const fetchGetDataWithAuthArrayBuffer = (uri) => {
    const token = localStorage.getItem('token');
    const url = `${API_BASE}${API_VERSION}${uri}`;
    try {
        return axios.get(url, {
            headers: { "Authorization": `Bearer ${token}` },
            responseType: 'arraybuffer'
        });
    } catch (error) {
        console.error('Error fetching data from url:', url, error.message);
        throw error;
    }
};

// GET Blob data with auth
const fetchGetBlobDataWithAuth = async (uri) => {
    const token = localStorage.getItem('token');
    const url = `${API_BASE}${API_VERSION}${uri}`;
    try {
        const response = await axios.get(url, {
            headers: { "Authorization": `Bearer ${token}` },
            responseType: "blob"
        });
        return response;
    } catch (error) {
        console.error('Error fetching data from url:', url, error.message);
        throw error;
    }
};

export default fetchGetData;
export {
    fetchPostData,
    fetchPostDataWithAuth,
    fetchGetDataWithAuth,
    fetchDeleteDataWithAuth,
    fetchPostFileUploadWithAuth,
    fetchPutDataWithAuth,
    fetchGetDataWithAuthArrayBuffer,
    fetchGetBlobDataWithAuth
};
