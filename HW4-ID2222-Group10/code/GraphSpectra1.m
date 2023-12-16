clear all;
close all;
clc;

% Load data from the file
%E = csvread('example1.dat');
E = csvread('example2.dat')

% Number of clusters (K = 4 or 2)
% K = 4; 
K = 2;

% Create a graph from the edges
col1= E(:,1);
col2= E(:,2);
max_ids = max(max(col1, col2));
As = sparse(col1, col2, 1, max_ids, max_ids);
A = full(adjacency(graph(As)));

% Display the adjacency matrix
figure(1)
spy(A);
title('Adjacency Matrix');

% Display the graph
figure(2)
plot(graph(A));
title('Graph Visualization');

% Laplacian matrix computation
L = laplacian(A);% Un-normalized
[V, d] = eigs(L, K);
figure(3)
[V1, D1] = eigs(un_norma_laplacian(A), K, 'SA');
plot(sort(V1(:, 2)), '-*')
title('Un-normalized Laplacian Matrix');
figure(4)
[V1, D1] = eigs(L, K, 'SA');
plot(sort(V1(:, 2)), '-*')
title('Normalized Laplacian Matrix');

% Laplacian Normalized function
function L = laplacian(A)
    D = diag(sum(A, 2));
    L = (D^(-1/2) * A * D^(-1/2));
end

% Laplacian Un-normalized function
function L = un_norma_laplacian(A)
    D = diag(sum(A, 2));
    L = D - A;
end