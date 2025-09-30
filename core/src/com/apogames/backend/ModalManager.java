package com.apogames.backend;

import com.apogames.entity.ApoButton;
import com.apogames.entity.ApoButtonImageDropdown;

import java.util.ArrayList;
import java.util.List;

/**
 * Modal Manager für Z-Order und Event-Priority Management
 * Verwaltet welche UI-Elemente modal (im Vordergrund) sind
 */
public class ModalManager {

    private static final List<ApoButtonImageDropdown> activeModals = new ArrayList<>();
    private static final List<ApoButton> allButtons = new ArrayList<>();

    // MouseOver-Tracking für Modals
    private static ApoButtonImageDropdown hoveredModal = null;
    private static int hoveredItemIndex = -1;

    /**
     * Registriert einen Button beim Modal Manager
     */
    public static void registerButton(ApoButton button) {
        if (!allButtons.contains(button)) {
            allButtons.add(button);
        }
    }

    /**
     * Entfernt einen Button vom Modal Manager
     */
    public static void unregisterButton(ApoButton button) {
        allButtons.remove(button);
        if (button instanceof ApoButtonImageDropdown) {
            activeModals.remove(button);
        }
    }

    /**
     * Öffnet ein Modal (Dropdown)
     */
    public static void openModal(ApoButtonImageDropdown modal) {
        if (!activeModals.contains(modal)) {
            activeModals.add(modal);
        }
    }

    /**
     * Schließt ein Modal
     */
    public static void closeModal(ApoButtonImageDropdown modal) {
        activeModals.remove(modal);
    }

    /**
     * Schließt alle Modals
     */
    public static void closeAllModals() {
        for (ApoButtonImageDropdown modal : new ArrayList<>(activeModals)) {
            modal.setSelect(false);
        }
        activeModals.clear();
    }

    /**
     * Prüft ob ein Event von einem Modal behandelt werden soll
     * @return Das Modal das das Event behandeln soll, oder null
     */
    public static ApoButtonImageDropdown getModalForEvent(int x, int y) {
        // Rückwärts durch die Liste (zuletzt geöffnet = höchste Priorität)
        for (int i = activeModals.size() - 1; i >= 0; i--) {
            ApoButtonImageDropdown modal = activeModals.get(i);
            if (modal.isVisible() && modal.intersects(x, y)) {
                return modal;
            }
        }
        return null;
    }

    /**
     * Gibt alle Buttons zurück die NICHT modal sind (für normale Event-Behandlung)
     */
    public static List<ApoButton> getNonModalButtons() {
        List<ApoButton> nonModalButtons = new ArrayList<>();
        for (ApoButton button : allButtons) {
            if (!(button instanceof ApoButtonImageDropdown) ||
                !activeModals.contains(button)) {
                nonModalButtons.add(button);
            }
        }
        return nonModalButtons;
    }

    /**
     * Gibt alle aktiven Modals zurück (für Rendering in korrekter Z-Order)
     */
    public static List<ApoButtonImageDropdown> getActiveModals() {
        return new ArrayList<>(activeModals);
    }

    /**
     * Prüft ob gerade ein Modal aktiv ist
     */
    public static boolean hasActiveModal() {
        return !activeModals.isEmpty();
    }

    /**
     * Updates MouseOver-Status für alle aktiven Modals
     */
    public static void updateMouseOver(int mouseX, int mouseY) {
        hoveredModal = null;
        hoveredItemIndex = -1;

        // Prüfe alle aktiven Modals (rückwärts für richtige Priorität)
        for (int i = activeModals.size() - 1; i >= 0; i--) {
            ApoButtonImageDropdown modal = activeModals.get(i);
            if (modal.isVisible() && modal.isSelect()) {
                int itemIndex = modal.getHoveredItemIndex(mouseX, mouseY);
                if (itemIndex >= 0) {
                    hoveredModal = modal;
                    hoveredItemIndex = itemIndex;
                    return; // Erstes (oberste) Modal gefunden
                }
            }
        }
    }

    /**
     * Gibt das aktuell gehovered Modal zurück
     */
    public static ApoButtonImageDropdown getHoveredModal() {
        return hoveredModal;
    }

    /**
     * Gibt den Index des gehovered Items zurück
     */
    public static int getHoveredItemIndex() {
        return hoveredItemIndex;
    }

    /**
     * Prüft ob ein bestimmtes Modal-Item gehovered ist
     */
    public static boolean isModalItemHovered(ApoButtonImageDropdown modal, int itemIndex) {
        return hoveredModal == modal && hoveredItemIndex == itemIndex;
    }

    /**
     * Debug-Information
     */
    public static void printStatus() {
        System.out.println("ModalManager Status:");
        System.out.println("- Registered Buttons: " + allButtons.size());
        System.out.println("- Active Modals: " + activeModals.size());
        System.out.println("- Hovered Modal: " + (hoveredModal != null ? hoveredModal.getFunction() : "none"));
        System.out.println("- Hovered Item Index: " + hoveredItemIndex);
        for (ApoButtonImageDropdown modal : activeModals) {
            System.out.println("  * Modal: " + modal.getFunction() + " at (" + modal.getX() + "," + modal.getY() + ")");
        }
    }
}