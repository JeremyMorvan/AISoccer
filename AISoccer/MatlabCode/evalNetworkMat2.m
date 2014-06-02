function out = evalNetworkMat2(xg,yg,xb,yb,x,y,W,V)

out = [];

for i=1:size(y,1)
    for j=1:size(y,2)
        out(i,j) = evalNetwork([xg;yg;xb;yb;x;atan2(y-yb,-xb);1],W,V);
    end
end

end

