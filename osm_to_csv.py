inputf = open("map.osm","r")
nodesf = open("osm_nodes.csv","w")
nodesf.write('X,Y,line_id,node_id,name\n')
linesf = open("osm_lines.csv","w")
linesf.write('id,highway,name,oneway,lit,lanes,maxspeed,railway,boundary,access,natural,barrier,tunnel,bridge,incline,waterway,busway,toll\n')


nodes = {}      # {'242134': ('37.9751383', '23.7254469'), '242135': ('37.9756041', '23.7257187'),...}
line=inputf.readline()
while(line != ''):
# for line in inputf.readlines():
    line=line.strip()   # without spaces
    parts=line.split(" ")
    TYPE=parts[0][1:]
    if (TYPE=="node"):
        # print("found node")
        ID,LAT,LON ='pipis','pipa','antegeia'
        for p in parts:
            if p[-2:]=='/>':p=p[:-2]
            if p[-1:]=='>':p=p[:-1]
            if p[:2]=='id':ID = p[4:-1]
            elif p[:3]=='lon':LON = p[5:-1]
            elif p[:3]=='lat':LAT = p[5:-1]
        
        nodes[ID]=(LAT,LON)
    
    elif (TYPE=="way"):
        ks = ['highway','name','oneway','lit','lanes','maxspeed','railway','boundary','access','natural','barrier','tunnel','bridge','incline','waterway','busway','toll']
        vs = {}
        for k in ks:
            vs[k]=''
        # print(nodes)
        # print("found way")
        ID = parts[1][4:-1]       
        # print(ID)
        line = inputf.readline()
        while(True):
            line=line.strip()   # without spaces
            parts=line.split(" ")
            TYPE=parts[0][1:]
            if (TYPE == "/way>"):break
            elif (TYPE == "nd"):
                nd_ID=parts[1][5:-3]
                nodesf.write(nodes[nd_ID][1]+','+nodes[nd_ID][0]+','+ID+','+nd_ID+',den exw\n')
            elif (TYPE == "tag"):
                k=parts[1][3:-1]
                v=parts[2][3:-3]
                if k in ks:
                    vs[k]=v
            line = inputf.readline()
        linesf.write(ID+',')
        for k in ks[:-1]:linesf.write(vs[k]+',')
        linesf.write(vs[ks[-1]]+'\n')
        # break

    # if (k==20):print(nodes);break
    line=inputf.readline()
