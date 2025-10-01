import React from 'react';
import { ChevronLeft } from 'lucide-react';

interface HeaderProps {
    title?: string;
    onBack?: () => void;
    username?: string;
}

const Header: React.FC<HeaderProps> = ({ title, onBack }) => (
    <header className="bg-white shadow-sm border-b border-gray-200 px-8 py-4">
        <div className="grid grid-cols-[auto,1fr,auto] items-center">
            <div>
                {onBack && (
                    <button
                        onClick={onBack}
                        className="flex items-center px-6 py-3 bg-white border border-gray-300 text-gray-700 rounded-xl hover:bg-gray-50 hover:border-gray-400 transition-all duration-200 font-medium shadow-sm hover:shadow-md"
                    >
                        <ChevronLeft className="w-5 h-5 mr-2" />
                        Retour
                    </button>
                )}
            </div>
            <div className="flex justify-center">
                {title && (
                    <h1 className="text-2xl font-bold text-gray-900 truncate">{title}</h1>
                )}
            </div>
            <div className="flex justify-end items-center">
                <a href="https://www.bmoi.mg/">
                    <img
                        src="/logo.png"
                        alt="BMOI Groupe BCP"
                        className="h-12 w-auto object-contain"
                    />
                </a>
            </div>
        </div>

    </header>
);

export default Header;
