clear all;
close all;
clc;

% Load data from the file
% E = csvread('example1.dat');
E = csvread('example2.dat')

% Number of clusters (K = 4 or 2)
% K = 4; 
K = 2;

% Create a graph from the edges
col1 = E(:,1);
col2 = E(:,2);
max_ids = max(max(col1, col2));
As = sparse(col1, col2, 1, max_ids, max_ids);
A = full(adjacency(graph(As)));

% Laplacian matrix computation
L = laplacian(A); % Un-normalized
[V, d] = eigs(L, K);

% Apply k-means to cluster the graph into K clusters
idx = kmeans(V, K);

% Display the clustered graph
figure(5)
h = plot(graph(A), 'MarkerSize', 6);
colors = {'magenta', 'red', 'blue', 'green'};
for i = 1:K
    cluster_indices = find(idx == i);
    highlight(h, cluster_indices, 'NodeColor', colors{i});
end
title('Clustered Graph');

% Display the non-clustered graph
figure(6)
plot(graph(A), 'MarkerSize', 6);
title('Non-Clustered Graph');

% Laplacian Normalized function
function L = laplacian(A)
    D = diag(sum(A, 2));
    L = (D^(-1/2) * A * D^(-1/2));
end
