/**
 * locations.js — Comprehensive Tamil Nadu Location Dataset & Search Utilities
 * KAVIN TRAVELS
 * Covering all 38 districts of Tamil Nadu, transit hubs, airports, stations, and tourist destinations.
 */

const TN_LOCATIONS = [
  // ==========================================
  // 1. POPULAR / FREQUENTLY BOOKED
  // ==========================================
  {
    id: 'coimbatore-city',
    name: 'Coimbatore',
    category: 'Popular',
    type: 'City & District HQ',
    district: 'Coimbatore',
    lat: 11.0168,
    lng: 76.9558,
    keywords: ['cbe', 'kovai', 'gandhipuram', 'rs puram', 'peelamedu'],
    icon: 'map-pin'
  },
  {
    id: 'ooty-nilgiris',
    name: 'Ooty (Udhagamandalam)',
    category: 'Popular',
    type: 'Hill Station & District HQ',
    district: 'Nilgiris',
    lat: 11.4102,
    lng: 76.6950,
    keywords: ['ooty', 'nilgiris', 'queen of hills', 'botanical garden'],
    icon: 'mountain'
  },
  {
    id: 'chennai-city',
    name: 'Chennai',
    category: 'Popular',
    type: 'Capital City & District HQ',
    district: 'Chennai',
    lat: 13.0827,
    lng: 80.2707,
    keywords: ['chennai', 'madras', 't nagar', 'guindy', 'velachery', 'central'],
    icon: 'map-pin'
  },
  {
    id: 'madurai-city',
    name: 'Madurai',
    category: 'Popular',
    type: 'Heritage City & District HQ',
    district: 'Madurai',
    lat: 9.9252,
    lng: 78.1198,
    keywords: ['madurai', 'temple city', 'meenakshi amman', 'mattuthavani'],
    icon: 'map-pin'
  },
  {
    id: 'kodaikanal',
    name: 'Kodaikanal',
    category: 'Popular',
    type: 'Hill Station',
    district: 'Dindigul',
    lat: 10.2381,
    lng: 77.4892,
    keywords: ['kodai', 'princess of hill stations', 'lake', 'pillar rocks'],
    icon: 'mountain'
  },
  {
    id: 'salem-city',
    name: 'Salem',
    category: 'Popular',
    type: 'City & District HQ',
    district: 'Salem',
    lat: 11.6643,
    lng: 78.1460,
    keywords: ['salem', 'steel city', 'new bus stand', 'junction'],
    icon: 'map-pin'
  },
  {
    id: 'tiruchirappalli-city',
    name: 'Tiruchirappalli (Trichy)',
    category: 'Popular',
    type: 'City & District HQ',
    district: 'Tiruchirappalli',
    lat: 10.7905,
    lng: 78.7047,
    keywords: ['trichy', 'rockfort', 'srirangam', 'chathiram', 'central bus stand'],
    icon: 'map-pin'
  },
  {
    id: 'rameswaram',
    name: 'Rameswaram',
    category: 'Popular',
    type: 'Pilgrimage & Coastal',
    district: 'Ramanathapuram',
    lat: 9.2876,
    lng: 79.3129,
    keywords: ['rameswaram', 'pamban', 'dhanushkodi', 'ramanathaswamy'],
    icon: 'landmark'
  },

  // ==========================================
  // 2. AIRPORTS
  // ==========================================
  {
    id: 'coimbatore-airport',
    name: 'Coimbatore International Airport (CJB)',
    category: 'Airports',
    type: 'International Airport',
    district: 'Coimbatore',
    lat: 11.0298,
    lng: 77.0434,
    keywords: ['cjb', 'coimbatore airport', 'peelamedu airport'],
    icon: 'plane'
  },
  {
    id: 'chennai-airport',
    name: 'Chennai International Airport (MAA)',
    category: 'Airports',
    type: 'International Airport',
    district: 'Chennai',
    lat: 12.9941,
    lng: 80.1709,
    keywords: ['maa', 'chennai airport', 'meenambakkam airport', 'domestic', 'international'],
    icon: 'plane'
  },
  {
    id: 'madurai-airport',
    name: 'Madurai Airport (IXM)',
    category: 'Airports',
    type: 'Customs Airport',
    district: 'Madurai',
    lat: 9.8345,
    lng: 78.0934,
    keywords: ['ixm', 'madurai airport', 'avaniyapuram'],
    icon: 'plane'
  },
  {
    id: 'trichy-airport',
    name: 'Tiruchirappalli International Airport (TRZ)',
    category: 'Airports',
    type: 'International Airport',
    district: 'Tiruchirappalli',
    lat: 10.7654,
    lng: 78.7126,
    keywords: ['trz', 'trichy airport'],
    icon: 'plane'
  },
  {
    id: 'tuticorin-airport',
    name: 'Tuticorin Airport (TCR)',
    category: 'Airports',
    type: 'Domestic Airport',
    district: 'Thoothukudi',
    lat: 8.7241,
    lng: 78.0267,
    keywords: ['tcr', 'thoothukudi airport', 'vaigaikulam'],
    icon: 'plane'
  },
  {
    id: 'salem-airport',
    name: 'Salem Airport (SXV)',
    category: 'Airports',
    type: 'Domestic Airport',
    district: 'Salem',
    lat: 11.7836,
    lng: 78.0653,
    keywords: ['sxv', 'salem airport', 'kamalapuram'],
    icon: 'plane'
  },

  // ==========================================
  // 3. RAILWAY JUNCTIONS & STATIONS
  // ==========================================
  {
    id: 'chennai-central-rail',
    name: 'Chennai Central Railway Station (MAS)',
    category: 'Railway Stations',
    type: 'Terminal Station',
    district: 'Chennai',
    lat: 13.0823,
    lng: 80.2755,
    keywords: ['mas', 'mgr central', 'puratchi thalaivar central'],
    icon: 'train'
  },
  {
    id: 'chennai-egmore-rail',
    name: 'Chennai Egmore Railway Station (MS)',
    category: 'Railway Stations',
    type: 'Major Junction',
    district: 'Chennai',
    lat: 13.0792,
    lng: 80.2612,
    keywords: ['ms', 'egmore station'],
    icon: 'train'
  },
  {
    id: 'coimbatore-junction-rail',
    name: 'Coimbatore Junction (CBE)',
    category: 'Railway Stations',
    type: 'Major Junction',
    district: 'Coimbatore',
    lat: 10.9983,
    lng: 76.9634,
    keywords: ['cbe', 'coimbatore railway station', 'main junction'],
    icon: 'train'
  },
  {
    id: 'madurai-junction-rail',
    name: 'Madurai Junction (MDU)',
    category: 'Railway Stations',
    type: 'Major Junction',
    district: 'Madurai',
    lat: 9.9189,
    lng: 78.1105,
    keywords: ['mdu', 'madurai railway station'],
    icon: 'train'
  },
  {
    id: 'trichy-junction-rail',
    name: 'Tiruchirappalli Junction (TPJ)',
    category: 'Railway Stations',
    type: 'Major Junction',
    district: 'Tiruchirappalli',
    lat: 10.7937,
    lng: 78.6856,
    keywords: ['tpj', 'trichy junction'],
    icon: 'train'
  },
  {
    id: 'salem-junction-rail',
    name: 'Salem Junction (SA)',
    category: 'Railway Stations',
    type: 'Major Divisional Junction',
    district: 'Salem',
    lat: 11.6775,
    lng: 78.1189,
    keywords: ['sa', 'salem junction'],
    icon: 'train'
  },
  {
    id: 'erode-junction-rail',
    name: 'Erode Junction (ED)',
    category: 'Railway Stations',
    type: 'Major Junction',
    district: 'Erode',
    lat: 11.3392,
    lng: 77.7225,
    keywords: ['ed', 'erode station'],
    icon: 'train'
  },
  {
    id: 'katpadi-junction-rail',
    name: 'Katpadi Junction (KPD)',
    category: 'Railway Stations',
    type: 'Major Junction',
    district: 'Vellore',
    lat: 12.9719,
    lng: 79.1368,
    keywords: ['kpd', 'katpadi', 'vellore station'],
    icon: 'train'
  },
  {
    id: 'tirunelveli-junction-rail',
    name: 'Tirunelveli Junction (TEN)',
    category: 'Railway Stations',
    type: 'Major Junction',
    district: 'Tirunelveli',
    lat: 8.7303,
    lng: 77.7083,
    keywords: ['ten', 'nellai station', 'tirunelveli junction'],
    icon: 'train'
  },
  {
    id: 'dindigul-junction-rail',
    name: 'Dindigul Junction (DG)',
    category: 'Railway Stations',
    type: 'Major Junction',
    district: 'Dindigul',
    lat: 10.3667,
    lng: 77.9833,
    keywords: ['dg', 'dindigul station'],
    icon: 'train'
  },

  // ==========================================
  // 4. HILL STATIONS
  // ==========================================
  {
    id: 'coonoor',
    name: 'Coonoor',
    category: 'Hill Stations',
    type: 'Hill Station',
    district: 'Nilgiris',
    lat: 11.3530,
    lng: 76.7959,
    keywords: ['coonoor', 'sims park', 'tea estates', 'nilgiris'],
    icon: 'mountain'
  },
  {
    id: 'kotagiri',
    name: 'Kotagiri',
    category: 'Hill Stations',
    type: 'Hill Station',
    district: 'Nilgiris',
    lat: 11.4243,
    lng: 76.8778,
    keywords: ['kotagiri', 'katherine falls', 'kodanad viewpoint'],
    icon: 'mountain'
  },
  {
    id: 'yercaud',
    name: 'Yercaud',
    category: 'Hill Stations',
    type: 'Hill Station',
    district: 'Salem',
    lat: 11.7753,
    lng: 78.2093,
    keywords: ['yercaud', 'shevaroy hills', 'jewel of the south'],
    icon: 'mountain'
  },
  {
    id: 'valparai',
    name: 'Valparai',
    category: 'Hill Stations',
    type: 'Hill Station & Tea Plateau',
    district: 'Coimbatore',
    lat: 10.3262,
    lng: 76.9554,
    keywords: ['valparai', 'anuradha', 'sholayar dam', 'aliyar'],
    icon: 'mountain'
  },
  {
    id: 'kolli-hills',
    name: 'Kolli Hills (Kolli Malai)',
    category: 'Hill Stations',
    type: 'Hill Station (70 Hairpin Bends)',
    district: 'Namakkal',
    lat: 11.2588,
    lng: 78.3424,
    keywords: ['kolli hills', 'agaya gangai', 'arapaleeswarar'],
    icon: 'mountain'
  },
  {
    id: 'meghamalai',
    name: 'Meghamalai (Highwavys)',
    category: 'Hill Stations',
    type: 'Hill Station & Cloud Mountain',
    district: 'Theni',
    lat: 9.7167,
    lng: 77.4167,
    keywords: ['meghamalai', 'highwavys', 'tea estate hills'],
    icon: 'mountain'
  },
  {
    id: 'topslip',
    name: 'Topslip (Anamalai Tiger Reserve)',
    category: 'Hill Stations',
    type: 'Wildlife & Hill Sanctuary',
    district: 'Coimbatore',
    lat: 10.4854,
    lng: 76.8402,
    keywords: ['topslip', 'pollachi', 'anamalai', 'parambikulam'],
    icon: 'mountain'
  },
  {
    id: 'jawadhu-hills',
    name: 'Jawadhu Hills',
    category: 'Hill Stations',
    type: 'Eastern Ghats Hill Range',
    district: 'Tiruvannamalai',
    lat: 12.6350,
    lng: 78.8500,
    keywords: ['jawadhu', 'kavalur observatory', 'beeman falls'],
    icon: 'mountain'
  },

  // ==========================================
  // 5. PILGRIMAGE PLACES
  // ==========================================
  {
    id: 'thanjavur-temple',
    name: 'Thanjavur (Brihadisvara Big Temple)',
    category: 'Pilgrimage Places',
    type: 'UNESCO World Heritage Temple',
    district: 'Thanjavur',
    lat: 10.7870,
    lng: 79.1378,
    keywords: ['thanjavur', 'tanjore', 'big temple', 'brihadeeswara'],
    icon: 'landmark'
  },
  {
    id: 'palani',
    name: 'Palani (Dhandayuthapani Swamy)',
    category: 'Pilgrimage Places',
    type: 'Murugan Arupadai Veedu',
    district: 'Dindigul',
    lat: 10.4500,
    lng: 77.5167,
    keywords: ['palani', 'murugan temple', 'dhandayuthapani'],
    icon: 'landmark'
  },
  {
    id: 'tiruchendur',
    name: 'Tiruchendur (Subramanya Swamy)',
    category: 'Pilgrimage Places',
    type: 'Seashore Murugan Shrine',
    district: 'Thoothukudi',
    lat: 8.4975,
    lng: 78.1219,
    keywords: ['tiruchendur', 'seashore temple', 'murugan'],
    icon: 'landmark'
  },
  {
    id: 'tiruvannamalai-temple',
    name: 'Tiruvannamalai (Annamalaiyar)',
    category: 'Pilgrimage Places',
    type: 'Pancha Bhoota Agni Sthalam',
    district: 'Tiruvannamalai',
    lat: 12.2253,
    lng: 79.0747,
    keywords: ['tiruvannamalai', 'girivalam', 'annamalaiyar', 'arunachala'],
    icon: 'landmark'
  },
  {
    id: 'chidambaram',
    name: 'Chidambaram (Nataraja Temple)',
    category: 'Pilgrimage Places',
    type: 'Pancha Bhoota Akasa Sthalam',
    district: 'Cuddalore',
    lat: 11.3992,
    lng: 79.6936,
    keywords: ['chidambaram', 'nataraja', 'thillai'],
    icon: 'landmark'
  },
  {
    id: 'kanchipuram-temples',
    name: 'Kanchipuram (City of 1000 Temples)',
    category: 'Pilgrimage Places',
    type: 'Silk & Heritage Temple City',
    district: 'Kanchipuram',
    lat: 12.8342,
    lng: 79.7036,
    keywords: ['kanchipuram', 'kanchi', 'ekambareswarar', 'kamakshi amman', 'silk'],
    icon: 'landmark'
  },
  {
    id: 'kumbakonam',
    name: 'Kumbakonam (Temple City & Mahamaham)',
    category: 'Pilgrimage Places',
    type: 'Heritage Navagraha Hub',
    district: 'Thanjavur',
    lat: 10.9602,
    lng: 79.3845,
    keywords: ['kumbakonam', 'navagraha temples', 'mahamaham', 'saranagapani'],
    icon: 'landmark'
  },
  {
    id: 'velankanni',
    name: 'Velankanni (Basilica of Our Lady)',
    category: 'Pilgrimage Places',
    type: 'Lourdes of the East',
    district: 'Nagapattinam',
    lat: 10.6800,
    lng: 79.8400,
    keywords: ['velankanni', 'church', 'basilica', 'lady of good health'],
    icon: 'landmark'
  },
  {
    id: 'nagore',
    name: 'Nagore (Dargah)',
    category: 'Pilgrimage Places',
    type: 'Historic Sufi Shrine',
    district: 'Nagapattinam',
    lat: 10.8167,
    lng: 79.8333,
    keywords: ['nagore', 'dargah', 'shahul hameed'],
    icon: 'landmark'
  },
  {
    id: 'srirangam',
    name: 'Srirangam (Sri Ranganathaswamy)',
    category: 'Pilgrimage Places',
    type: 'Largest Hindu Temple Complex',
    district: 'Tiruchirappalli',
    lat: 10.8624,
    lng: 78.6908,
    keywords: ['srirangam', 'ranganathar', 'rajagopuram'],
    icon: 'landmark'
  },
  {
    id: 'thiruthani',
    name: 'Thiruthani (Murugan Temple)',
    category: 'Pilgrimage Places',
    type: 'Murugan Arupadai Veedu',
    district: 'Tiruvallur',
    lat: 13.1814,
    lng: 79.6108,
    keywords: ['thiruthani', 'tiruttani', 'murugan'],
    icon: 'landmark'
  },
  {
    id: 'swamimalai',
    name: 'Swamimalai (Murugan Temple)',
    category: 'Pilgrimage Places',
    type: 'Murugan Arupadai Veedu',
    district: 'Thanjavur',
    lat: 10.9542,
    lng: 79.3331,
    keywords: ['swamimalai', 'murugan', 'kumbakonam'],
    icon: 'landmark'
  },
  {
    id: 'pazhamudircholai',
    name: 'Pazhamudircholai & Alagar Kovil',
    category: 'Pilgrimage Places',
    type: 'Murugan & Vishnu Shrine',
    district: 'Madurai',
    lat: 10.0833,
    lng: 78.2333,
    keywords: ['pazhamudircholai', 'alagar kovil', 'murugan'],
    icon: 'landmark'
  },

  // ==========================================
  // 6. TOURIST & HERITAGE DESTINATIONS
  // ==========================================
  {
    id: 'kanyakumari-cape',
    name: 'Kanyakumari (Cape Comorin)',
    category: 'Tourist Destinations',
    type: 'Triveni Sangam & Memorial',
    district: 'Kanyakumari',
    lat: 8.0883,
    lng: 77.5385,
    keywords: ['kanyakumari', 'vivekananda rock', 'thiruvalluvar statue', 'sunrise'],
    icon: 'landmark'
  },
  {
    id: 'mahabalipuram',
    name: 'Mahabalipuram (Mamallapuram)',
    category: 'Tourist Destinations',
    type: 'UNESCO Shore Temples',
    district: 'Chengalpattu',
    lat: 12.6269,
    lng: 80.1927,
    keywords: ['mahabalipuram', 'mamallapuram', 'shore temple', 'five rathas', 'ecr'],
    icon: 'landmark'
  },
  {
    id: 'courtallam',
    name: 'Courtallam (Kutralam Waterfalls)',
    category: 'Tourist Destinations',
    type: 'Spa of South India',
    district: 'Tenkasi',
    lat: 8.9298,
    lng: 77.2764,
    keywords: ['courtallam', 'kutralam', 'main falls', 'five falls', 'tenkasi'],
    icon: 'mountain'
  },
  {
    id: 'hogenakkal',
    name: 'Hogenakkal Waterfalls',
    category: 'Tourist Destinations',
    type: 'Niagara of India',
    district: 'Dharmapuri',
    lat: 12.1189,
    lng: 77.7770,
    keywords: ['hogenakkal', 'cauvery falls', 'coracle ride', 'dharmapuri'],
    icon: 'mountain'
  },
  {
    id: 'puducherry-pondicherry',
    name: 'Puducherry (Pondicherry)',
    category: 'Tourist Destinations',
    type: 'French Quarter & Beaches',
    district: 'Puducherry (UT)',
    lat: 11.9416,
    lng: 79.8083,
    keywords: ['pondicherry', 'puducherry', 'auroville', 'promenade beach', 'french colony'],
    icon: 'map-pin'
  },
  {
    id: 'dhanushkodi',
    name: 'Dhanushkodi (Ghost Town & Beach)',
    category: 'Tourist Destinations',
    type: 'End of India Point',
    district: 'Ramanathapuram',
    lat: 9.1764,
    lng: 79.4167,
    keywords: ['dhanushkodi', 'arichal munai', 'rameswaram'],
    icon: 'landmark'
  },
  {
    id: 'pichavaram',
    name: 'Pichavaram Mangrove Forest',
    category: 'Tourist Destinations',
    type: '2nd Largest Mangrove in World',
    district: 'Cuddalore',
    lat: 11.4286,
    lng: 79.7806,
    keywords: ['pichavaram', 'mangrove', 'boating', 'chidambaram'],
    icon: 'mountain'
  },

  // ==========================================
  // 7. ALL 38 TAMIL NADU DISTRICT HEADQUARTERS & TOWNS
  // ==========================================
  {
    id: 'ariyalur',
    name: 'Ariyalur',
    category: 'Cities & Towns',
    type: 'District HQ & Cement Hub',
    district: 'Ariyalur',
    lat: 11.1401,
    lng: 79.0786,
    keywords: ['ariyalur', 'gangaikonda cholapuram'],
    icon: 'map-pin'
  },
  {
    id: 'chengalpattu',
    name: 'Chengalpattu',
    category: 'Cities & Towns',
    type: 'District HQ & Transit Hub',
    district: 'Chengalpattu',
    lat: 12.6841,
    lng: 79.9836,
    keywords: ['chengalpattu', 'gst road', 'mahindra world city'],
    icon: 'map-pin'
  },
  {
    id: 'cuddalore',
    name: 'Cuddalore',
    category: 'Cities & Towns',
    type: 'District HQ & Port City',
    district: 'Cuddalore',
    lat: 11.7480,
    lng: 79.7714,
    keywords: ['cuddalore', 'silver beach', 'port'],
    icon: 'map-pin'
  },
  {
    id: 'dharmapuri',
    name: 'Dharmapuri',
    category: 'Cities & Towns',
    type: 'District HQ',
    district: 'Dharmapuri',
    lat: 12.1211,
    lng: 78.1582,
    keywords: ['dharmapuri', 'mango city'],
    icon: 'map-pin'
  },
  {
    id: 'dindigul',
    name: 'Dindigul',
    category: 'Cities & Towns',
    type: 'District HQ & Lock City',
    district: 'Dindigul',
    lat: 10.3673,
    lng: 77.9803,
    keywords: ['dindigul', 'rock fort', 'biryani', 'lock city'],
    icon: 'map-pin'
  },
  {
    id: 'erode',
    name: 'Erode',
    category: 'Cities & Towns',
    type: 'District HQ & Turmeric City',
    district: 'Erode',
    lat: 11.3410,
    lng: 77.7172,
    keywords: ['erode', 'turmeric city', 'textile hub', 'bhavani'],
    icon: 'map-pin'
  },
  {
    id: 'hosur',
    name: 'Hosur',
    category: 'Cities & Towns',
    type: 'Industrial Hub',
    district: 'Krishnagiri',
    lat: 12.7409,
    lng: 77.8253,
    keywords: ['hosur', 'sipcot', 'bangalore border', 'automobile hub'],
    icon: 'map-pin'
  },
  {
    id: 'kallakurichi',
    name: 'Kallakurichi',
    category: 'Cities & Towns',
    type: 'District HQ',
    district: 'Kallakurichi',
    lat: 11.7383,
    lng: 78.9639,
    keywords: ['kallakurichi', 'kalrayan hills'],
    icon: 'map-pin'
  },
  {
    id: 'karur',
    name: 'Karur',
    category: 'Cities & Towns',
    type: 'District HQ & Textile Capital',
    district: 'Karur',
    lat: 10.9601,
    lng: 78.0766,
    keywords: ['karur', 'textile export', 'pasupatheeswarar'],
    icon: 'map-pin'
  },
  {
    id: 'krishnagiri',
    name: 'Krishnagiri',
    category: 'Cities & Towns',
    type: 'District HQ',
    district: 'Krishnagiri',
    lat: 12.5186,
    lng: 78.2137,
    keywords: ['krishnagiri', 'dam', 'mango city'],
    icon: 'map-pin'
  },
  {
    id: 'mayiladuthurai',
    name: 'Mayiladuthurai',
    category: 'Cities & Towns',
    type: 'District HQ',
    district: 'Mayiladuthurai',
    lat: 11.1075,
    lng: 79.6523,
    keywords: ['mayiladuthurai', 'mayavaram', 'cauvery delta'],
    icon: 'map-pin'
  },
  {
    id: 'nagapattinam',
    name: 'Nagapattinam',
    category: 'Cities & Towns',
    type: 'District HQ & Coastal Port',
    district: 'Nagapattinam',
    lat: 10.7672,
    lng: 79.8449,
    keywords: ['nagapattinam', 'port', 'sea coast'],
    icon: 'map-pin'
  },
  {
    id: 'nagercoil',
    name: 'Nagercoil',
    category: 'Cities & Towns',
    type: 'District HQ (Kanyakumari District)',
    district: 'Kanyakumari',
    lat: 8.1833,
    lng: 77.4119,
    keywords: ['nagercoil', 'nagaraja temple', 'kanyakumari hq'],
    icon: 'map-pin'
  },
  {
    id: 'namakkal',
    name: 'Namakkal',
    category: 'Cities & Towns',
    type: 'District HQ & Poultry Hub',
    district: 'Namakkal',
    lat: 11.2189,
    lng: 78.1674,
    keywords: ['namakkal', 'anjaneyar temple', 'fort', 'transport hub'],
    icon: 'map-pin'
  },
  {
    id: 'perambalur',
    name: 'Perambalur',
    category: 'Cities & Towns',
    type: 'District HQ',
    district: 'Perambalur',
    lat: 11.2342,
    lng: 78.8820,
    keywords: ['perambalur', 'ranjankudi fort'],
    icon: 'map-pin'
  },
  {
    id: 'pollachi',
    name: 'Pollachi',
    category: 'Cities & Towns',
    type: 'Coconut & Cinema Hub',
    district: 'Coimbatore',
    lat: 10.6585,
    lng: 77.0088,
    keywords: ['pollachi', 'coconut city', 'anamalai gateway'],
    icon: 'map-pin'
  },
  {
    id: 'pudukkottai',
    name: 'Pudukkottai',
    category: 'Cities & Towns',
    type: 'District HQ & Heritage City',
    district: 'Pudukkottai',
    lat: 10.3833,
    lng: 78.8001,
    keywords: ['pudukkottai', 'sittannavasal', 'heritage palace'],
    icon: 'map-pin'
  },
  {
    id: 'ramanathapuram',
    name: 'Ramanathapuram (Ramnad)',
    category: 'Cities & Towns',
    type: 'District HQ',
    district: 'Ramanathapuram',
    lat: 9.3639,
    lng: 78.8395,
    keywords: ['ramanathapuram', 'ramnad', 'sethupathy palace'],
    icon: 'map-pin'
  },
  {
    id: 'ranipet',
    name: 'Ranipet',
    category: 'Cities & Towns',
    type: 'District HQ & Industrial City',
    district: 'Ranipet',
    lat: 12.9271,
    lng: 79.3330,
    keywords: ['ranipet', 'sipcot', 'leather hub'],
    icon: 'map-pin'
  },
  {
    id: 'sivaganga',
    name: 'Sivaganga',
    category: 'Cities & Towns',
    type: 'District HQ',
    district: 'Sivaganga',
    lat: 9.8433,
    lng: 78.4809,
    keywords: ['sivaganga', 'chettinad', 'maruthu pandiyar'],
    icon: 'map-pin'
  },
  {
    id: 'sivakasi',
    name: 'Sivakasi',
    category: 'Cities & Towns',
    type: 'Printing & Fireworks Capital',
    district: 'Virudhunagar',
    lat: 9.4533,
    lng: 77.7946,
    keywords: ['sivakasi', 'kutti japan', 'printing', 'crackers'],
    icon: 'map-pin'
  },
  {
    id: 'tenkasi',
    name: 'Tenkasi',
    category: 'Cities & Towns',
    type: 'District HQ & Kasi Viswanathar',
    district: 'Tenkasi',
    lat: 8.9594,
    lng: 77.3150,
    keywords: ['tenkasi', 'kasi viswanathar', 'western ghats foot'],
    icon: 'map-pin'
  },
  {
    id: 'theni',
    name: 'Theni',
    category: 'Cities & Towns',
    type: 'District HQ & Cardamom Hub',
    district: 'Theni',
    lat: 10.0104,
    lng: 77.4768,
    keywords: ['theni', 'suruli falls', 'periyakulam', 'bodinayakanur'],
    icon: 'map-pin'
  },
  {
    id: 'thoothukudi',
    name: 'Thoothukudi (Tuticorin)',
    category: 'Cities & Towns',
    type: 'District HQ & Pearl City Port',
    district: 'Thoothukudi',
    lat: 8.7642,
    lng: 78.1348,
    keywords: ['thoothukudi', 'tuticorin', 'pearl city', 'v o c port', 'salt pans'],
    icon: 'map-pin'
  },
  {
    id: 'tirunelveli',
    name: 'Tirunelveli (Nellai)',
    category: 'Cities & Towns',
    type: 'District HQ & Halwa City',
    district: 'Tirunelveli',
    lat: 8.7139,
    lng: 77.7567,
    keywords: ['tirunelveli', 'nellai', 'nellaiappar', 'halwa', 'palayamkottai'],
    icon: 'map-pin'
  },
  {
    id: 'tirupattur',
    name: 'Tirupattur',
    category: 'Cities & Towns',
    type: 'District HQ',
    district: 'Tirupattur',
    lat: 12.4947,
    lng: 78.5678,
    keywords: ['tirupattur', 'yelagiri hills gateway'],
    icon: 'map-pin'
  },
  {
    id: 'tiruppur',
    name: 'Tiruppur',
    category: 'Cities & Towns',
    type: 'District HQ & Knitwear Capital',
    district: 'Tiruppur',
    lat: 11.1085,
    lng: 77.3411,
    keywords: ['tiruppur', 'knitwear city', 'dollar city', 'garments export'],
    icon: 'map-pin'
  },
  {
    id: 'tiruvallur',
    name: 'Tiruvallur',
    category: 'Cities & Towns',
    type: 'District HQ & Veeraraghava Temple',
    district: 'Tiruvallur',
    lat: 13.1438,
    lng: 79.9083,
    keywords: ['tiruvallur', 'veeraraghava swamy', 'poondi'],
    icon: 'map-pin'
  },
  {
    id: 'tiruvarur',
    name: 'Tiruvarur',
    category: 'Cities & Towns',
    type: 'District HQ & Thyagaraja Temple',
    district: 'Tiruvarur',
    lat: 10.7725,
    lng: 79.6366,
    keywords: ['tiruvarur', 'chariot temple', 'thyagarajar'],
    icon: 'map-pin'
  },
  {
    id: 'vellore',
    name: 'Vellore',
    category: 'Cities & Towns',
    type: 'District HQ & Historic Fort City',
    district: 'Vellore',
    lat: 12.9165,
    lng: 79.1325,
    keywords: ['vellore', 'fort', 'cmc hospital', 'golden temple sripuram'],
    icon: 'map-pin'
  },
  {
    id: 'villupuram',
    name: 'Viluppuram (Villupuram)',
    category: 'Cities & Towns',
    type: 'District HQ & Major Railway Cross',
    district: 'Viluppuram',
    lat: 11.9401,
    lng: 79.4861,
    keywords: ['villupuram', 'gingee fort gateway'],
    icon: 'map-pin'
  },
  {
    id: 'virudhunagar',
    name: 'Virudhunagar',
    category: 'Cities & Towns',
    type: 'District HQ & Commerce Hub',
    district: 'Virudhunagar',
    lat: 9.5872,
    lng: 77.9514,
    keywords: ['virudhunagar', 'kamarajar illam', 'oil and spice market'],
    icon: 'map-pin'
  },
  {
    id: 'sivakasi',
    name: 'Sivakasi',
    category: 'Cities & Towns',
    type: 'Industrial Hub & Printing Capital',
    district: 'Virudhunagar',
    lat: 9.4533,
    lng: 77.7947,
    keywords: ['sivakasi', 'fireworks city', 'printing', 'matches', 'virudhunagar'],
    icon: 'map-pin'
  },

  // ==========================================
  // 8. NEIGHBORING POPULAR OUTSTATION HUBS
  // ==========================================
  {
    id: 'bengaluru-bangalore',
    name: 'Bengaluru (Bangalore)',
    category: 'Cities & Towns',
    type: 'Interstate Capital Hub',
    district: 'Bengaluru Urban, KA',
    lat: 12.9716,
    lng: 77.5946,
    keywords: ['bangalore', 'bengaluru', 'silk board', 'electronic city', 'majestic'],
    icon: 'map-pin'
  },
  {
    id: 'mysuru-mysore',
    name: 'Mysuru (Mysore)',
    category: 'Tourist Destinations',
    type: 'Heritage Palace City',
    district: 'Mysuru, KA',
    lat: 12.2958,
    lng: 76.6394,
    keywords: ['mysore', 'mysuru', 'palace', 'chamundi hills'],
    icon: 'landmark'
  },
  {
    id: 'munnar-kerala',
    name: 'Munnar',
    category: 'Hill Stations',
    type: 'Misty Tea Hills',
    district: 'Idukki, KL',
    lat: 10.0889,
    lng: 77.0595,
    keywords: ['munnar', 'tea estates', 'mattupetty', 'eravikulam'],
    icon: 'mountain'
  },
  {
    id: 'kochi-cochin',
    name: 'Kochi (Cochin)',
    category: 'Cities & Towns',
    type: 'Queen of Arabian Sea',
    district: 'Ernakulam, KL',
    lat: 9.9312,
    lng: 76.2673,
    keywords: ['kochi', 'cochin', 'fort kochi', 'marine drive'],
    icon: 'map-pin'
  },
  {
    id: 'palakkad',
    name: 'Palakkad (Palghat)',
    category: 'Cities & Towns',
    type: 'Gateway of Kerala',
    district: 'Palakkad, KL',
    lat: 10.7867,
    lng: 76.6548,
    keywords: ['palakkad', 'fort', 'malampuzha', 'coimbatore border'],
    icon: 'map-pin'
  }
];

// Helper: Filter locations by query string across name, type, district, and keywords
function searchLocations(query) {
  if (!query || query.trim() === '') {
    return TN_LOCATIONS;
  }
  const q = query.trim().toLowerCase();
  return TN_LOCATIONS.filter(loc => {
    if (loc.name.toLowerCase().includes(q)) return true;
    if (loc.district.toLowerCase().includes(q)) return true;
    if (loc.type.toLowerCase().includes(q)) return true;
    if (loc.category.toLowerCase().includes(q)) return true;
    if (loc.keywords && loc.keywords.some(k => k.toLowerCase().includes(q))) return true;
    return false;
  });
}

// Helper: Group an array of location objects by category
function groupLocationsByCategory(locationsList) {
  const categoryOrder = [
    'Popular',
    'Airports',
    'Railway Stations',
    'Hill Stations',
    'Pilgrimage Places',
    'Tourist Destinations',
    'Cities & Towns'
  ];

  const grouped = {};
  categoryOrder.forEach(cat => {
    grouped[cat] = [];
  });

  locationsList.forEach(loc => {
    const cat = loc.category || 'Cities & Towns';
    if (!grouped[cat]) grouped[cat] = [];
    grouped[cat].push(loc);
  });

  return grouped;
}

// Expose to window
window.TN_LOCATIONS = TN_LOCATIONS;
window.searchLocations = searchLocations;
window.groupLocationsByCategory = groupLocationsByCategory;
