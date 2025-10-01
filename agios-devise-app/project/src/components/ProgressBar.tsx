// components/ProgressBar.tsx
import React from 'react';

interface ProgressBarProps {
    current: number;
    total: number;
    percentage: number;
    status: string;
}

const ProgressBar: React.FC<ProgressBarProps> = ({ current, total, percentage, status }) => {
    const getStatusText = () => {
        switch (status) {
            case 'processing':
                return 'Traitement en cours...';
            case 'completed':
                return 'Terminé';
            case 'error':
                return 'Erreur';
            default:
                return 'En attente';
        }
    };

    const getStatusColor = () => {
        switch (status) {
            case 'processing':
                return 'bg-blue-500';
            case 'completed':
                return 'bg-green-500';
            case 'error':
                return 'bg-red-500';
            default:
                return 'bg-gray-300';
        }
    };

    return (
        <div className="w-full bg-white rounded-xl shadow-sm border border-gray-200 p-6 mt-4">
            <div className="flex justify-between items-center mb-3">
                <span className="text-sm font-medium text-gray-700">
                    Progression de la vérification
                </span>
                <span className="text-sm text-gray-500">
                    {getStatusText()}
                </span>
            </div>

            <div className="w-full bg-gray-200 rounded-full h-3 mb-2">
                <div
                    className={`h-3 rounded-full transition-all duration-300 ${getStatusColor()}`}
                    style={{ width: `${percentage}%` }}
                ></div>
            </div>

            <div className="flex justify-between items-center">
                <span className="text-xs text-gray-500">
                    {current} / {total} comptes traités
                </span>
                <span className="text-xs font-medium text-gray-700">
                    {percentage}%
                </span>
            </div>

            {status === 'processing' && (
                <div className="mt-3 flex items-center">
                    <div className="w-3 h-3 border-2 border-blue-500 border-t-transparent rounded-full animate-spin mr-2"></div>
                    <span className="text-xs text-blue-500">Traitement en cours...</span>
                </div>
            )}
        </div>
    );
};

export default ProgressBar;