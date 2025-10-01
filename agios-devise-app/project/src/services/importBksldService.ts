import axios from 'axios';

export const importBksldService = async (date: string, onProgress?: (percent: number) => void) => {
    const response = await axios.post(
        'http://localhost:8080/api/import/bksld',
        null,
        {
            params: { date },
            headers: { Accept: 'application/json' },
            withCredentials: true,
            onUploadProgress: progressEvent => {
                if (onProgress && progressEvent.total) {
                    const percent = Math.round((progressEvent.loaded * 100) / progressEvent.total);
                    onProgress(percent);
                }
            }
        }
    );
    return response;
};