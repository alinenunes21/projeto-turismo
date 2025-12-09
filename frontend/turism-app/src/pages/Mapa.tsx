import { useEffect, useRef } from 'react';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';

// Corrigir ícones do Leaflet
// delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
});

const Mapa = ({ latitude, longitude, nome } : { latitude: any, longitude: any, nome: any }) => {
  const mapRef = useRef(null);
  const mapInstanceRef = useRef<any>(null);

  useEffect(() => {
    if (!mapRef.current || !latitude || !longitude) return;

    // Inicializar mapa
    if (!mapInstanceRef.current) {
      mapInstanceRef.current = L.map(mapRef.current).setView([latitude, longitude], 15);

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
      }).addTo(mapInstanceRef.current);
    }

    // Adicionar marcador
    const marker = L.marker([latitude, longitude]).addTo(mapInstanceRef.current);
    marker.bindPopup(`<b>${nome}</b>`).openPopup();

    return () => {
      if (mapInstanceRef.current) {
        mapInstanceRef.current?.remove();
        mapInstanceRef.current = null;
      }
    };
  }, [latitude, longitude, nome]);

  return (
    <div>
      <div ref={mapRef} className="w-full h-64 rounded-lg border border-gray-300"></div>
      <a
        href={`https://www.google.com/maps/dir/?api=1&destination=${latitude},${longitude}`}
        target="_blank"
        rel="noopener noreferrer"
        className="inline-block mt-3 text-purple-600 hover:text-purple-700 font-medium"
      >
        Como chegar →
      </a>
    </div>
  );
};

export default Mapa;