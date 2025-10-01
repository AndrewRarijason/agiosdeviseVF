import React, { useEffect, useState } from 'react';
import {ChevronLeft, ChevronRight, LogOut, Calculator, Users, History, LayoutDashboard, User} from 'lucide-react';
import { useNavigate, Link } from 'react-router-dom';

interface SidebarProps {
    currentPage?: string;
}

const Sidebar: React.FC<SidebarProps> = ({ currentPage = 'ACCUEIL' }) => {
    const [isCollapsed, setIsCollapsed] = useState(false);
    const [role, setRole] = useState<string | null>(null); // <-- Ajout du state manquant
    const [username, setUsername] = useState<string>('');
    const navigate = useNavigate();

    useEffect(() => {
        fetch('/validate-token', {
            method: 'POST',
            credentials: 'include'
        })
            .then(res => res.json())
            .then(data => {
                setRole(data.role || null);
                setUsername(data.username || '');
            })
            .catch(() => {
                setRole(null);
                setUsername('');
            });
    }, []);


    const menuItems = [
        {
            id: 'accueil',
            label: 'ACCUEIL',
            icon: null,
            path: '/accueil'
        },

        ...(role === 'Admin' || role === 'Pupitreur' ? [
        {
            id: 'calcul',
            label: 'Calcul arrêtés trimestriels',
            icon: Calculator,
            path: '/calcul-arretes'
        }] : []),


        ...(role === 'Admin'
            ? [{
                id: 'gestion',
                label: 'Gestion utilisateurs',
                icon: Users,
                path: '/gestion-utilisateurs'
            }]
            : []),
        {
            id: 'historique',
            label: 'Historique arrêtés',
            icon: History,
            path: '/historique-arrete'
        },
        {
            id: 'dashboard',
            label: 'Tableau de bord',
            icon : LayoutDashboard,
            path: '/dashboard'
        }
    ];

    const handleLogout = async () => {
        try {
            await fetch('/api/logout', {
                method: 'POST',
                credentials: 'include'
            });
        } catch {
            // Optionnel : gestion d’erreur
        } finally {
            navigate('/');
        }
    };

    const handleMenuClick = (path: string) => {
        navigate(path);
    };

    return (
        <div className={`fixed left-0 top-0 h-full bg-[#581D74] text-white transition-all duration-300 z-50 ${
            isCollapsed ? 'w-16' : 'w-64'
        }`}>
            {/* Toggle Button */}
            <button
                onClick={() => setIsCollapsed(!isCollapsed)}
                className="absolute -right-3 top-6 bg-white text-[#581D74] rounded-full p-1 shadow-lg hover:shadow-xl transition-all duration-200"
            >
                {isCollapsed ? (
                    <ChevronRight className="w-4 h-4" />
                ) : (
                    <ChevronLeft className="w-4 h-4" />
                )}
            </button>

            {/* Sidebar Content */}
            <div className="flex flex-col h-full">
                {/* Menu Items */}
                <div className="flex-1 pt-8">
                    {menuItems.map((item) => (
                        <div key={item.id} className="mb-2">
                            {item.id === 'accueil' ? (
                                <div className={`px-6 py-4 ${currentPage === 'ACCUEIL' ? 'bg-white/10' : ''}`}>
                                    {!isCollapsed && (
                                        <Link
                                            to="/accueil"
                                            className="text-2xl font-bold tracking-wide cursor-pointer"
                                        >
                                            ACCUEIL
                                        </Link>
                                    )}
                                </div>
                            ) : (
                                <button
                                    onClick={() => handleMenuClick(item.path)}
                                    className={`w-full flex items-center px-6 py-3 text-left hover:bg-white/10 transition-colors duration-200 ${
                                        currentPage === item.label ? 'bg-white/10 border-r-4 border-white' : ''
                                    }`}
                                >
                                    {item.icon && (
                                        <item.icon className={`w-5 h-5 ${isCollapsed ? 'mx-auto' : 'mr-3'}`} />
                                    )}
                                    {!isCollapsed && (
                                        <span className="text-sm font-medium">{item.label}</span>
                                    )}
                                </button>
                            )}
                        </div>
                    ))}
                </div>

                {/* Logout Button */}
                {/* Username + Logout */}
                <div className="p-4 flex flex-col items-center">
                    {/* Affichage du nom d'utilisateur */}
                    {!isCollapsed && username && (
                        <div className="mb-4 flex items-end self-start space-x-2 text-white/80 pb-2 px-2">
                            <User className="w-8 h-8" />
                            <span className="text-m">{username}</span>
                        </div>
                    )}
                    <button
                        onClick={handleLogout}
                        className="w-full flex items-center justify-center bg-white/20 hover:bg-white/30 rounded-full py-3 px-4 transition-all duration-200"
                    >
                        <LogOut className={`w-5 h-5 ${isCollapsed ? '' : 'mr-2'}`} />
                        {!isCollapsed && (
                            <span className="text-sm font-medium">Déconnexion</span>
                        )}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Sidebar;