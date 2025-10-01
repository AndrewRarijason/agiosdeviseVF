import React, { useState } from 'react';
import {Calendar} from 'lucide-react';
import Sidebar from "./Sidebar";
import Header from "./Header";
import RepartitionAgios from "./RepartitionAgios";
import HistogrammeAgios from "./HistogrammeAgios";
import DashboardCards from "./DashboardCards";
import { Trimestre } from '../types';
import { useDashboardArrete } from '../hooks/useDashboardArrete';
import { useNavigate} from "react-router-dom";

const trimestres: { label: Trimestre; value: Trimestre }[] = [
    { label: 'T1', value: 'T1' },
    { label: 'T2', value: 'T2' },
    { label: 'T3', value: 'T3' },
    { label: 'T4', value: 'T4' },
];

const DashboardResultat: React.FC = () => {
    const [trimestre, setTrimestre] = useState<Trimestre>('T1');
    const [annee, setAnnee] = useState(new Date().getFullYear());
    const [periodeValidee, setPeriodeValidee] = useState<{ debut: string; fin: string } | null>(null);
    const navigate = useNavigate();
    const handleBack = () => {
        navigate('/accueil');
    };
    const { data, loading, error, fetchDashboard } = useDashboardArrete();

    const periodeTrimestre: Record<Trimestre, { debut: string; fin: string }> = {
        T1: { debut: `${annee}-01-01`, fin: `${annee}-03-31` },
        T2: { debut: `${annee}-04-01`, fin: `${annee}-06-30` },
        T3: { debut: `${annee}-07-01`, fin: `${annee}-09-30` },
        T4: { debut: `${annee}-10-01`, fin: `${annee}-12-31` },
    };

    const handleValider = (e: React.FormEvent) => {
        e.preventDefault();
        const periode = periodeTrimestre[trimestre];
        setPeriodeValidee(periode);
        fetchDashboard(periode.debut, periode.fin);
    };

    const formatNumber = (num: number) => new Intl.NumberFormat('fr-FR').format(num);

    const formatPeriode = (dateDebut: string, dateFin: string) => {
        const debut = new Date(dateDebut);
        const fin = new Date(dateFin);
        const moisDebut = debut.toLocaleString('fr-FR', { month: 'long' });
        const moisFin = fin.toLocaleString('fr-FR', { month: 'long' });
        const annee = debut.getFullYear();
        return `${moisDebut.charAt(0).toUpperCase() + moisDebut.slice(1)}-${moisFin.charAt(0).toUpperCase() + moisFin.slice(1)} ${annee}`;
    };

    const formatDureeCalcul = (debut: string, fin: string) => {
        const d1 = new Date(debut);
        const d2 = new Date(fin);
        let diffMs = d2.getTime() - d1.getTime();
        const jours = Math.floor(diffMs / (24 * 3600 * 1000));
        diffMs %= 24 * 3600 * 1000;
        const heures = Math.floor(diffMs / (3600 * 1000));
        diffMs %= 3600 * 1000;
        const minutes = Math.floor(diffMs / (60 * 1000));
        diffMs %= 60 * 1000;
        const secondes = Math.floor(diffMs / 1000);
        const tierces = Math.floor((diffMs % 1000) / 10);
        return `${jours}j ${heures.toString().padStart(2, '0')}h ${minutes.toString().padStart(2, '0')}m ${secondes.toString().padStart(2, '0')}s ${tierces.toString().padStart(2, '0')}t`;
    };

    return (
        <div className="min-h-screen">
            <Sidebar currentPage="ACCUEIL" />
            <div className="flex-1 ml-64 transition-all duration-300 flex flex-col min-h-screen">
                <Header title="TABLEAU DE BORD" onBack={handleBack}/>
                <main className="p-8">
                    <div className="max-w-7xl mx-auto">
                        <div className="bg-white/80 backdrop-blur-lg rounded-xl p-6 mb-6 border border-[#CCA8DD]/30 shadow-lg">
                            <div className="flex items-center justify-between mb-8">
                                <div className="flex items-center space-x-3">
                                    <div className="p-3 bg-gradient-to-br from-[#581D74] to-[#6B2C87] rounded-xl">
                                        <Calendar className="w-6 h-6 text-white" />
                                    </div>
                                    <div>
                                        <h2 className="text-2xl font-bold text-gray-900">Sélection de la Période</h2>
                                        <p className="text-gray-600 mt-1">Choisissez le trimestre à analyser</p>
                                    </div>
                                </div>
                                <form className="flex flex-col sm:flex-row items-end gap-6" onSubmit={handleValider}>
                                    <div className="flex items-center gap-4">
                                        <div className="space-y-2">
                                            <label className="block text-sm font-semibold text-gray-700">Trimestre</label>
                                            <select
                                                value={trimestre}
                                                onChange={e => setTrimestre(e.target.value as Trimestre)}
                                                className="px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200 font-medium bg-white hover:border-gray-400"
                                            >
                                                {trimestres.map((t) => (
                                                    <option key={t.value} value={t.value}>
                                                        {t.label}
                                                    </option>
                                                ))}
                                            </select>
                                        </div>
                                        <div className="space-y-2">
                                            <label className="block text-sm font-semibold text-gray-700">Année</label>
                                            <input
                                                type="number"
                                                min="2000"
                                                max="2100"
                                                value={annee}
                                                onChange={e => setAnnee(Number(e.target.value))}
                                                className="px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200 font-medium bg-white hover:border-gray-400 w-32"
                                            />
                                        </div>
                                    </div>
                                    <button
                                        type="submit"
                                        className={`flex items-center px-8 py-3 bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white rounded-xl hover:from-[#4A1562] hover:to-[#581D74] transition-all duration-200 font-semibold shadow-lg hover:shadow-xl transform hover:scale-105 ${
                                            loading ? 'opacity-50 cursor-not-allowed' : ''
                                        }`}
                                        disabled={loading}
                                    >
                                        {loading ? (
                                            <>
                                                <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
                                                Chargement...
                                            </>
                                        ) : (
                                            <>
                                                Afficher
                                            </>
                                        )}
                                    </button>
                                </form>
                            </div>
                        </div>
                        <div className="bg-white/80 backdrop-blur-lg rounded-xl p-6 border border-[#CCA8DD]/30 shadow-lg">
                            <DashboardCards
                                periodeValidee={periodeValidee}
                                loading={loading}
                                error={error}
                                data={data}
                                formatNumber={formatNumber}
                                formatPeriode={formatPeriode}
                                formatDureeCalcul={formatDureeCalcul}
                            />
                            {periodeValidee && (
                                <RepartitionAgios
                                    dateDebutArrete={periodeValidee.debut}
                                    dateFinArrete={periodeValidee.fin}
                                />
                            )}
                            {periodeValidee && (
                                <HistogrammeAgios
                                    dateDebutArrete={periodeValidee.debut}
                                    dateFinArrete={periodeValidee.fin}
                                />
                            )}
                        </div>
                    </div>
                </main>
            </div>
        </div>
    );
};

export default DashboardResultat;