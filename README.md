# Adaptive Zero-Sum RSP Strategy System

2023 Spring
객체지향프로그래밍 수업 팀 프로젝트

랜덤 행렬 기반 Zero-sum 가위바위보 환경에서다중 전략을 확률적으로 조정하며 점수를 극대화하는
적응형 전략 시스템 설계


## Overview
본 프로젝트는 무작위로 생성되는 Gain Matrix를 기반으로 하는 Rock-Scissors-Paper 게임에서 점수를 극대화하는 전략 시스템을 설계한 프로젝트입니다.

단순 승패가 아닌, 매 판 생성되는 Zero-sum Gain Matrix에 따라 승리해도 점수를 잃고, 패배해도 점수를 얻을 수 있는 구조를 갖습니다.

이에 따라 본 팀은 단일 전략이 아닌 다중 전략을 확률적으로 선택하고 성과에 따라 확률을 동적으로 조정하는 적응형 전략을 구현하였습니다.


## Core Concepts

- Zero-sum Game 구조
- Gain Matrix 기반 의사결정
- 다중 전략 확률 선택 모델
- 평균 성과 기반 확률 재조정
- 3차 Markov Chain 기반 상대 패턴 분석
- Java 객체지향 설계



## Strategy

### 1) 손실 최소화 전략 (MinimumDamage)

- 행/열 분석을 통해 손실 최소화
- 양수 성분이 많은 선택 우선
- 안정적 점수 확보

---

### 2) 최대 이익 전략 (MaxBenefit)

- Gain Matrix 내 최대 절댓값 선택
- 단기 점수 극대화 전략

---

### 3) 마코프 체인 전략 (Markov Chain)

- 상대 이전 3턴 분석 (3rd-order Markov)
- 조건부 확률 기반 예측
- 패턴 대응 전략

---

### 4) 완전 무작위 전략 (Random)

- 예측 불가능성 확보
- 전략 다양성 유지


## Adaptive Probability Adjustment Mechanism

- 초기 100회: 마코프 체인(3번 전략) 제외 3가지 전략 균등 확률
- 이후 4가지 전략 확률 기반 선택
- 500회마다 전략 평균 점수 계산
- 최대 0.3 범위 내에서 확률 조정

> 성과가 좋은 전략은 확률 증가  
> 성과가 낮은 전략은 확률 감소  


** 3번 전략인 마코프 체인의 경우에는 분석을 위해 이전 상대의 움직임에 대한 기록이 있어야 하므로 시행횟수인 index가 100미만인 경우에는 마코프 체인을 제외한 3개의 전략인 동일한 약 33%의 확률로 실행될 수 있도록 구성


## System Architecture

Game Engine  
│  
├── Gain Matrix Generator  
├── Strategy Selector  
│   ├── MinimumDamage  
│   ├── MaxBenefit  
│   ├── MarkovStrategy  
│   └── RandomStrategy  
├── Probability Adjustment Module  
└── GUI Renderer 
