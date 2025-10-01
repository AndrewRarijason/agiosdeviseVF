import React, { useState } from 'react';
import { User, Lock, Eye, EyeOff } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';


const LoginPage: React.FC = () => {
    useEffect(() => {
        document.cookie = "authToken=; Max-Age=0; path=/; secure; SameSite=Strict";
        localStorage.removeItem('authToken');
    }, []);
    const [identifier, setIdentifier] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setError('');

        try {
            // Envoie les identifiants au backend, le cookie JWT sera posé automatiquement
            const response = await fetch('/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `username=${encodeURIComponent(identifier)}&password=${encodeURIComponent(password)}`,
                credentials: 'include'
            });

            let data: Record<string, unknown> = {};
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                data = await response.json();
            }

            if (response.ok && (data as { success?: boolean }).success) {
                // Vérifie la validité du token via le cookie HttpOnly
                const validateRes = await fetch('/validate-token', {
                    method: 'POST',
                    credentials: 'include'
                });

                if (validateRes.ok) {
                    navigate('/accueil');
                } else {
                    setError('Token invalide');
                }
            } else {
                setError((data as { error?: string }).error || 'Identifiants incorrects');
            }
        } catch {
            setError('Erreur de connexion au serveur');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-screen relative overflow-hidden bg-gradient-to-br from-[#581D74] via-[#581D74]/70 to-[#581D74]">
            {/* Abstract Background Shapes */}
            <div className="absolute inset-0">
                <div className="absolute top-0 left-0 w-full h-full">
                    <div className="absolute top-[-10%] right-[-10%] w-96 h-96 bg-gradient-to-br from-purple-300/30 to-purple-500/20 rounded-full blur-3xl transform rotate-12"></div>
                    <div className="absolute bottom-[-15%] left-[-15%] w-[500px] h-[500px] bg-gradient-to-tr from-purple-500/20 to-purple-700/30 rounded-full blur-3xl transform -rotate-45"></div>
                    <div className="absolute top-[20%] left-[10%] w-64 h-64 bg-gradient-to-br from-purple-200/20 to-purple-400/10 rounded-full blur-2xl"></div>
                    <div className="absolute bottom-[30%] right-[15%] w-72 h-72 bg-gradient-to-tl from-purple-600/20 to-purple-400/15 rounded-full blur-2xl transform rotate-45"></div>
                </div>
            </div>

            {/* Main Content */}
            <div className="relative z-10 min-h-screen flex items-center justify-center px-4 sm:px-6 lg:px-8">
                <div className="max-w-md w-full space-y-8">
                    {/* Logo Section */}
                    <div className="text-center">
                        <div className="flex justify-center mb-2 -mt-12">
                            <div className="relative">
                                <img
                                    src="/BMOI.svg"
                                    alt="Logo BMOI"
                                    className="w-[285px] h-[240px] object-contain"
                                />
                            </div>
                        </div>
                        <p className="text-black/80 text-sm font-medium tracking-wide"></p>
                    </div>

                    {/* Login Form */}
                    <div>
                        <form onSubmit={handleSubmit} className="space-y-6">
                            {/* Identifier Field */}
                            <div>
                                <div className="relative">
                                    <User className="absolute left-3 top-1/2 transform -translate-y-1/2 text-purple-950 h-5 w-5" />
                                    <input
                                        type="text"
                                        value={identifier}
                                        onChange={(e) => setIdentifier(e.target.value)}
                                        placeholder="IDENTIFIANT"
                                        className="w-full pl-12 pr-4 py-4 bg-purple-50/50 border border-[#CCA8DD] rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200 placeholder-black/40 text-black/90"
                                        required
                                    />
                                </div>
                            </div>

                            {/* Password Field */}
                            <div>
                                <div className="relative">
                                    <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 text-purple-950 h-5 w-5" />
                                    <input
                                        type={showPassword ? 'text' : 'password'}
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        placeholder="MOT DE PASSE"
                                        className="w-full pl-12 pr-4 py-4 bg-purple-50/50 border border-[#CCA8DD] rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200 placeholder-black/40 text-black/90"
                                        required
                                    />
                                    <button
                                        type="button"
                                        onClick={() => setShowPassword(!showPassword)}
                                        className="absolute right-3 top-1/2 transform -translate-y-1/2 text-black/50 hover:text-black/80 transition-colors"
                                    >
                                        {showPassword ? <EyeOff className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
                                    </button>
                                </div>
                            </div>

                            {/* Submit Button */}
                            <button
                                type="submit"
                                disabled={isLoading}
                                className={`w-full py-4 px-6 rounded-xl font-semibold text-purple-700 bg-white border-2 border-purple-200 hover:bg-purple-50 hover:border-purple-300 focus:ring-2 focus:ring-purple-400 focus:border-transparent transition-all duration-200 ${
                                    isLoading ? 'opacity-70 cursor-not-allowed' : 'transform hover:-translate-y-0.5 active:translate-y-0'
                                }`}
                            >
                                {isLoading ? (
                                    <div className="flex items-center justify-center">
                                        <div className="w-5 h-5 border-2 border-purple-400 border-t-transparent rounded-full animate-spin mr-2"></div>
                                        Connexion en cours...
                                    </div>
                                ) : (
                                    'SE CONNECTER'
                                )}
                            </button>
                        </form>
                        {error && (
                            <div className="mt-4 text-center text-red-500 text-sm">
                                {error}
                            </div>
                        )}
                    </div>

                    {/* Footer */}
                    <div className="text-center text-white/60 text-xs">
                        <p>© 2025 BMOI Groupe BCP</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;