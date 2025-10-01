import React, { useState } from 'react';
import Sidebar from './Sidebar';
import Header from './Header';
import Footer from './Footer';
import { useNavigate } from "react-router-dom";
import { ChevronLeft, ChevronRight, Calculator, UsersRound, History } from "lucide-react";
import { useUserRole } from '../hooks/useUserRole';

const Accueil: React.FC = () => {
    const navigate = useNavigate();
    const role = useUserRole();
    const [currentCard, setCurrentCard] = useState(0);

    const cards = [
        {
            key: 'calcul',
            show: role === 'Admin' || role === 'Pupitreur',
            content: (
                <div className="relative bg-white rounded-xl shadow-lg border border-gray-200 overflow-hidden h-full flex flex-col">
                    <div
                        className="absolute inset-0 bg-cover bg-center z-0"
                        style={{ backgroundImage: "url('/calcul-arrete.jpg')" }}
                    >
                        <div className="absolute inset-0 bg-black/40"></div>
                    </div>
                    <div className="relative z-10 flex-1 flex flex-col p-8">
                        <div className="flex-1 flex flex-col">
                            <div className="flex items-center mb-6">
                                <div className="w-16 h-16 bg-white/20 backdrop-blur-sm rounded-lg flex items-center justify-center border border-white/30">
                                    <Calculator className="text-white w-8 h-8" />
                                </div>
                                <h3 className="ml-6 text-3xl font-semibold text-white">
                                    Calcul arrêtés trimestriels
                                </h3>
                            </div>
                            <p className="text-white/90 text-lg backdrop-blur-sm bg-black/20 p-8 rounded-lg mt-16">
                                Calculez et générez les arrêtés trimestriels pour vos opérations.
                            </p>
                        </div>
                        <div className="flex justify-center pt-8">
                            <button
                                onClick={() => navigate('/calcul-arretes')}
                                className="bg-white text-[#581D74] py-3 px-8 rounded-lg
                                   transition-all duration-500 ease-in-out
                                   font-medium border border-white/30 text-lg
                                   hover:bg-[#581D74] hover:text-white
                                   hover:shadow-lg hover:shadow-[#581D74]/25"
                            >
                                Accéder
                            </button>
                        </div>
                    </div>
                </div>
            )
        },
        {
            key: 'gestion',
            show: role === 'Admin',
            content: (
                <div className="relative bg-white rounded-xl shadow-lg border border-gray-200 overflow-hidden h-full flex flex-col">
                    <div
                        className="absolute inset-0 bg-cover bg-center z-0"
                        style={{ backgroundImage: "url('/gestion-utilisateur.jpg')" }}
                    >
                        <div className="absolute inset-0 bg-black/40"></div>
                    </div>
                    <div className="relative z-10 flex-1 flex flex-col p-8">
                        <div className="flex-1 flex flex-col">
                            <div className="flex items-center mb-6">
                                <div className="w-16 h-16 bg-white/20 backdrop-blur-sm rounded-lg flex items-center justify-center border border-white/30">
                                    <UsersRound className="text-white w-8 h-8" />
                                </div>
                                <h3 className="ml-6 text-3xl font-semibold text-white">
                                    Gestion utilisateurs
                                </h3>
                            </div>
                            <p className="text-white/90 text-lg backdrop-blur-sm bg-black/20 p-8 rounded-lg mt-16">
                                Gérez les comptes utilisateurs et leurs permissions d'accès.
                            </p>
                        </div>
                        <div className="flex justify-center pt-8">
                            <button
                                onClick={() => navigate('/gestion-utilisateurs')}
                                className="bg-white text-[#581D74] py-3 px-8 rounded-lg
                                   transition-all duration-500 ease-in-out
                                   font-medium border border-white/30 text-lg
                                   hover:bg-[#581D74] hover:text-white
                                   hover:shadow-lg hover:shadow-[#581D74]/25"
                            >
                                Accéder
                            </button>
                        </div>
                    </div>
                </div>
            )
        },
        {
            key: 'historique',
            show: true,
            content: (
                <div className="relative bg-white rounded-xl shadow-lg border border-gray-200 overflow-hidden h-full flex flex-col">
                    <div
                        className="absolute inset-0 bg-cover bg-center z-0"
                        style={{ backgroundImage: "url('/historique-calcul.jpg')" }}
                    >
                        <div className="absolute inset-0 bg-black/40"></div>
                    </div>
                    <div className="relative z-10 flex-1 flex flex-col p-8">
                        <div className="flex-1 flex flex-col">
                            <div className="flex items-center mb-6">
                                <div className="w-16 h-16 bg-white/20 backdrop-blur-sm rounded-lg flex items-center justify-center border border-white/30">
                                    <History className="text-white w-8 h-8" />
                                </div>
                                <h3 className="ml-6 text-3xl font-semibold text-white">
                                    Historique arrêté
                                </h3>
                            </div>
                            <p className="text-white/90 text-lg backdrop-blur-sm bg-black/20 p-8 rounded-lg mt-16">
                                Consultez l'historique complet des arrêtés générés par période trimestrielle.
                            </p>
                        </div>
                        <div className="flex justify-center pt-8">
                            <button
                                onClick={() => navigate('/historique-arrete')}
                                className="bg-white text-[#581D74] py-3 px-8 rounded-lg
                                   transition-all duration-500 ease-in-out
                                   font-medium border border-white/30 text-lg
                                   hover:bg-[#581D74] hover:text-white
                                   hover:shadow-lg hover:shadow-[#581D74]/25"
                            >
                                Accéder
                            </button>
                        </div>
                    </div>
                </div>
            )
        }
    ].filter(card => card.show);

    if (role === null) return null;

    const nextCard = () => setCurrentCard((prev) => (prev === cards.length - 1 ? 0 : prev + 1));
    const prevCard = () => setCurrentCard((prev) => (prev === 0 ? cards.length - 1 : prev - 1));

    return (
        <div className="min-h-screen bg-gray-100 flex">
            <Sidebar currentPage="ACCUEIL" />
            <div className="flex-1 ml-64 transition-all duration-300 flex flex-col min-h-screen">
                <Header title="Traitement d'arrêtés trimestriels des comptes en devise - BMOI" />
                <main className="flex-1 flex flex-col">
                    <div className="flex-1 flex flex-col">
                        <div className="p-8 pb-4"></div>
                        <div className="flex items-center justify-center flex-1 px-50">
                            <button
                                onClick={prevCard}
                                className="p-3 rounded-full bg-gray-100 hover:bg-gray-200 transition-colors duration-200 mr-4 z-10"
                            >
                                <ChevronLeft className="w-6 h-6 text-gray-600" />
                            </button>
                            <div className="flex-1 h-full">
                                {cards[currentCard]?.content}
                            </div>
                            <button
                                onClick={nextCard}
                                className="p-3 rounded-full bg-gray-100 hover:bg-gray-200 transition-colors duration-200 ml-4 z-10"
                            >
                                <ChevronRight className="w-6 h-6 text-gray-600" />
                            </button>
                        </div>
                        <div className="flex justify-center space-x-2 p-8 pt-4">
                            {cards.map((_, index) => (
                                <button
                                    key={index}
                                    onClick={() => setCurrentCard(index)}
                                    className={`w-3 h-3 rounded-full transition-colors duration-200 ${
                                        currentCard === index ? 'bg-[#581D74]' : 'bg-gray-300'
                                    }`}
                                />
                            ))}
                        </div>
                    </div>
                </main>
                <Footer />
            </div>
        </div>
    );
};

export default Accueil;
