import React, { useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import Sidebar from '../components/Sidebar';
import Header from '../components/Header';
import Footer from '../components/Footer';
import { useNavigate } from 'react-router-dom';
import { useHistoriqueArreteDetail } from '../hooks/useHistoriqueArretes';

const labels: Record<string, string> = {
    ncp: "Numéro de compte",
    nomrest: "NomRest",
    dev: "Code devise",
    soldeFinalCalcule: "Solde final calculé",
    dateDebutArrete: "Date début arrêté",
    dateFinArrete: "Date fin arrêté",
    sumMvtCrediteur: "Total mouvements créditeurs",
    tauxCrediteur: "Taux créditeur",
    interetCrediteur: "Intérêt créditeur",
    sumMvtDebiteur: "Total mouvements débiteurs",
    tauxDebiteur: "Taux débiteur",
    interetDebiteur: "Intérêt débiteur",
    netAgios: "Net agios",
    age: "Code agence"
};

const HistoriqueArreteDetail: React.FC = () => {
    const [searchParams] = useSearchParams();
    const { data, loading, error, getDetail } = useHistoriqueArreteDetail();
    const navigate = useNavigate();
    const handleBack = () => {
        navigate('/historique-arrete');
    };

    useEffect(() => {
        const ncp = searchParams.get('ncp');
        const trimestre = searchParams.get('trimestre');
        const annee = searchParams.get('annee');
        if (ncp && trimestre && annee) {
            getDetail(ncp, trimestre, annee);
        }
    }, [searchParams]);

    return (
        <div className="min-h-screen bg-gray-100 flex flex-col">
            <div className="flex flex-1">
                <Sidebar currentPage="HISTORIQUE" />
                <div className="flex-1 ml-64 transition-all duration-300">
                    <Header title="Détails de l'arrêté trimestriel" onBack={handleBack}/>
                    <main className="p-8">
                        <div className="max-w-7xl mx-auto space-y-8">
                            {loading && <span className="text-gray-500">Chargement...</span>}
                            {error && (
                                <div className="bg-red-50 border border-red-200 rounded-lg p-3 mb-4">
                                    <span className="text-red-500 text-sm">{error}</span>
                                </div>
                            )}
                            {data && (
                                <>
                                    <div className="flex items-center justify-between p-4 bg-gray-50 rounded-lg mb-4">
                                        <h2 className="text-lg font-semibold text-gray-900">
                                            Détail du compte {data.ncp}
                                        </h2>
                                    </div>
                                    <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
                                        <table className="min-w-full text-[15px]">
                                            <tbody>
                                            {Object.entries(data).map(([key, value]) => (
                                                <tr key={key}>
                                                    <td className="px-4 py-2 font-medium text-gray-700">
                                                        {labels[key] ?? key}
                                                    </td>
                                                    <td className="px-4 py-2 text-gray-900 text-left">{String(value)}</td>
                                                </tr>
                                            ))}
                                            </tbody>
                                        </table>
                                    </div>
                                </>
                            )}
                        </div>
                    </main>
                </div>
            </div>
            <Footer />
        </div>
    );
};

export default HistoriqueArreteDetail;