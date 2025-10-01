import React, { useEffect } from "react";
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid, Cell } from "recharts";
import { useAgiosTcli } from "../hooks/useAgiosTcli";
import { CustomTooltip } from "./CustomTooltip";
import { BarChart3, Loader2, AlertCircle } from "lucide-react";

interface AgiosTcliData {
    type_client: string;
    dev: string;
    dateDebutArrete: string;
    libele: string;
    tcli: number;
    cdalpha: string;
    dateFinArrete: string;
    totalNetAgios: number;
    xLabel: string;
}

interface Props {
    dateDebutArrete: string;
    dateFinArrete: string;
}

const TCLI_COLORS: Record<number, string> = {
    1: "#3B82F6", // Blue
    2: "#581D74", // Primary purple
    3: "#10B981", // Green
};

const TCLI_LABELS: Record<number, string> = {
    1: "Client particulier",
    2: "Société",
    3: "Entreprise individuelle",
};

const renderLegend = () => (
    <div className="flex flex-wrap justify-center gap-6 mt-6 p-4 bg-gray-50 rounded-xl">
        {Object.entries(TCLI_COLORS).map(([tcli, color]) => (
            <div key={tcli} className="flex items-center gap-3">
                <div
                    className="w-4 h-4 rounded-full shadow-sm"
                    style={{ backgroundColor: color }}
                />
                <span className="text-sm font-medium text-gray-700">{TCLI_LABELS[parseInt(tcli)]}</span>
            </div>
        ))}
    </div>
);

const HistogrammeAgios: React.FC<Props> = ({ dateDebutArrete, dateFinArrete }) => {
    const { data, loading, error, fetchAgiosTcli } = useAgiosTcli();

    useEffect(() => {
        if (dateDebutArrete && dateFinArrete) {
            fetchAgiosTcli(dateDebutArrete, dateFinArrete);
        }
    }, [dateDebutArrete, dateFinArrete, fetchAgiosTcli]);

    if (!dateDebutArrete || !dateFinArrete) return null;

    const chartData = (data as AgiosTcliData[]).map(item => ({
        ...item,
        xLabel: `${item.cdalpha} - ${item.tcli}`,
        cdalpha: item.cdalpha,
        tcli: item.tcli,
        totalNetAgios: item.totalNetAgios
    }));

    const formatDate = (dateStr: string) => {
        return new Date(dateStr).toLocaleDateString('fr-FR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    };

    return (
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
            <div className="p-8">
                {/* Header */}
                <div className="flex items-center justify-between mb-8">
                    <div className="flex items-center space-x-3">
                        <div className="p-3 bg-gradient-to-br from-[#581D74] to-[#6B2C87] rounded-xl">
                            <BarChart3 className="w-6 h-6 text-white" />
                        </div>
                        <div>
                            <h2 className="text-2xl font-bold text-gray-900">Top 3 - Agios par Type de Client</h2>
                            <p className="text-gray-600 mt-1">
                                Période: {formatDate(dateDebutArrete)} - {formatDate(dateFinArrete)}
                            </p>
                        </div>
                    </div>
                </div>

                {/* Content */}
                <div className="relative">
                    {loading && (
                        <div className="flex items-center justify-center py-16">
                            <div className="flex items-center space-x-3">
                                <Loader2 className="w-6 h-6 text-[#581D74] animate-spin" />
                                <span className="text-gray-600 font-medium">Chargement des données...</span>
                            </div>
                        </div>
                    )}

                    {error && (
                        <div className="flex items-center justify-center py-16">
                            <div className="flex flex-col items-center space-y-4">
                                <div className="p-3 bg-red-100 rounded-full">
                                    <AlertCircle className="w-8 h-8 text-red-600" />
                                </div>
                                <div className="text-center">
                                    <p className="text-red-800 font-semibold">Erreur de chargement</p>
                                    <p className="text-red-600 text-sm mt-1">{error}</p>
                                </div>
                            </div>
                        </div>
                    )}

                    {!loading && !error && chartData.length === 0 && (
                        <div className="flex items-center justify-center py-16">
                            <div className="flex flex-col items-center space-y-4">
                                <div className="p-3 bg-gray-100 rounded-full">
                                    <BarChart3 className="w-8 h-8 text-gray-400" />
                                </div>
                                <div className="text-center">
                                    <p className="text-gray-600 font-semibold">Aucune donnée disponible</p>
                                    <p className="text-gray-500 text-sm mt-1">
                                        Aucun agio trouvé pour la période sélectionnée
                                    </p>
                                </div>
                            </div>
                        </div>
                    )}

                    {!loading && !error && chartData.length > 0 && (
                        <div className="space-y-6">
                            {/* Chart */}
                            <div className="bg-gradient-to-br from-gray-50 to-white rounded-xl p-6 border border-gray-100">
                                <ResponsiveContainer width="100%" height={400}>
                                    <BarChart
                                        data={chartData}
                                        barCategoryGap="30%"
                                        barGap={8}
                                        margin={{ top: 20, right: 30, left: 20, bottom: 60 }}
                                    >
                                        <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                                        <XAxis
                                            dataKey="xLabel"
                                            tick={({ x, y, payload }) => {
                                                const [dev] = payload.value.split(" - ");
                                                return (
                                                    <text
                                                        x={x}
                                                        y={y + 10}
                                                        textAnchor="middle"
                                                        fill="#6B7280"
                                                        fontSize={12}
                                                        fontWeight="500"
                                                    >
                                                        {dev}
                                                    </text>
                                                );
                                            }}
                                            axisLine={{ stroke: '#E5E7EB' }}
                                            tickLine={{ stroke: '#E5E7EB' }}
                                        />
                                        <YAxis
                                            axisLine={{ stroke: '#E5E7EB' }}
                                            tickLine={{ stroke: '#E5E7EB' }}
                                            tick={{ fill: '#6B7280', fontSize: 12 }}
                                        />
                                        <Tooltip content={<CustomTooltip />} />
                                        <Bar
                                            dataKey="totalNetAgios"
                                            name="Agios"
                                            radius={[4, 4, 0, 0]}
                                        >
                                            {chartData.map((entry, index) => (
                                                <Cell
                                                    key={`cell-${index}`}
                                                    fill={TCLI_COLORS[entry.tcli] || "#8B5CF6"}
                                                />
                                            ))}
                                        </Bar>
                                    </BarChart>
                                </ResponsiveContainer>
                            </div>

                            {/* Legend */}
                            {renderLegend()}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default HistogrammeAgios;